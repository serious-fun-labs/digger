package digger.web

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import digger.elasticsearch.{ClusterHealthLevel, DocsStats, IndexSettings, StoreStats}
import digger.web.ApiProtocol._
import spray.json._

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

trait ElasticsearchApiPart extends ApiPart {
  abstract override def subRoutes: List[Route] = route :: super.subRoutes

  private val status = get {
    withRequestTimeout(30.seconds) {
      val configuration =
        ElasticsearchConfiguration(esClient.configuration.serviceUrl)
      val resultsFuture = for {
        stats <- esClient.stats("*")
        clusterHealth <- esClient.clusterHealth(ClusterHealthLevel.Indices)
      } yield (stats, clusterHealth)
      onComplete(resultsFuture) {
        case Success((stats, clusterHealth)) =>
          complete(
            ElasticsearchStatus(
              configuration,
              cluster = Some(
                ElasticsearchClusterStatus(
                  clusterHealth.name,
                  clusterHealth.status.toString,
                  clusterHealth.numberOfNodes,
                  stats.all.primaries.docs.getOrElse(DocsStats()).count,
                  stats.indices.map {
                    case (name, indexStatsAggs) =>
                      val health = clusterHealth.indices
                        .getOrElse(Map())
                        .get(name)
                        .map(_.status.toString)
                        .getOrElse("")
                      ElasticsearchIndexStatus(
                        name,
                        health,
                        indexStatsAggs.primaries.docs.getOrElse(DocsStats()).count,
                        indexStatsAggs.primaries.store.getOrElse(StoreStats()).sizeInBytes
                      )
                  }.toSeq
                )
              )
            )
          )
        case Failure(exception) =>
          complete(ElasticsearchStatus(configuration, error = Some(exception.getMessage)))
      }
    }
  }

  private val resetSample = get {
    val indexName = "sample"
    val resetFuture = for {
      exists <- esClient.indexExists(indexName)
      deleted <- if (exists) esClient.deleteIndex(indexName) else Future(true)
      created <-
        if (deleted) {
          esClient.createIndex(
            indexName,
            Some(IndexSettings(numberOfShards = Some("1"), numberOfReplicas = Some("0")))
          )
        } else throw new Exception("Failed to delete old sample index.")
      checkedCreated <-
        if (created) Future(true) else throw new Exception("Failed to create new sample index.")
      indexed1 <- esClient.indexDocument(indexName, JsObject("hello" -> JsString("world")))
      indexed2 <- esClient.indexDocument(indexName, JsObject("hello" -> JsString("again")))
      ok <-
        if (checkedCreated && indexed1 && indexed2) Future(true)
        else throw new Exception("Failed to index document.")
    } yield ok

    onComplete(resetFuture) {
      case Success(_) => complete(HttpResponse(StatusCodes.OK))
      case Failure(exception) =>
        complete(HttpResponse(StatusCodes.InternalServerError, entity = exception.getMessage))
    }
  }

  private val route =
    pathPrefix("elasticsearch") {
      concat(
        path("status") {
          status
        },
        path("reset-sample") {
          resetSample
        }
      )
    }
}
