package digger.web

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import digger.elasticsearch.{ClusterHealthLevel, EsClient}
import digger.web.ApiProtocol._

import scala.concurrent.ExecutionContextExecutor

class Api(
    implicit val actorSystem: ActorSystem,
    implicit val esClient: EsClient
) extends ApiBase
    with SearchApiPart
    with ElasticsearchApiPart {
  override implicit val executionContext: ExecutionContextExecutor =
    actorSystem.dispatcher

  lazy val route: Route =
    pathPrefix("api") {
      concat(subRoutes: _*)
    }
}

abstract class ApiPart {
  implicit val executionContext: ExecutionContextExecutor
  implicit val esClient: EsClient

  protected def subRoutes: List[Route]
}

abstract class ApiBase extends ApiPart {
  protected override def subRoutes: List[Route] = List()
}

trait SearchApiPart extends ApiPart {
  abstract override def subRoutes: List[Route] = route :: super.subRoutes

  private lazy val route =
    path("search") {
      get {
        parameter("query") { query =>
          if (query != null && query.nonEmpty) {
            Thread.sleep(1000)
            complete(
              s"Thank you for searching '$query' with us! But your results are in another version."
            )
          } else {
            Thread.sleep(250)
            complete(
              HttpResponse(
                StatusCodes.BadRequest,
                entity =
                  "Empty queries are prohibited for no particular reason."
              )
            )
          }
        }
      }
    }
}

trait ElasticsearchApiPart extends ApiPart {
  abstract override def subRoutes: List[Route] = route :: super.subRoutes

  private lazy val route =
    pathPrefix("elasticsearch") {
      concat(
        path("status") {
          get {
            val configuration =
              ElasticsearchConfiguration(esClient.configuration.serviceUrl)
            val resultsFuture = for {
              indices <- esClient.index("*")
              clusterHealth <-
                esClient.clusterHealth(ClusterHealthLevel.Indices)
            } yield (indices, clusterHealth)
            onSuccess(resultsFuture) {
              (indices, clusterHealth) =>
                complete(
                  ElasticsearchStatus(
                    configuration,
                    cluster = Some(
                      ElasticsearchClusterStatus(
                        clusterHealth.name,
                        clusterHealth.status.toString,
                        clusterHealth.numberOfNodes,
                        indices.map {
                          case (name, _) =>
                            val health = clusterHealth.indices
                              .getOrElse(Map())
                              .get(name)
                              .map(_.status.toString)
                              .getOrElse("")
                            ElasticsearchIndexStatus(name, health)
                        }.toSeq
                      )
                    )
                  )
                )
            }
          }
        },
        path("reset-sample") {
          get {
            Thread.sleep(500)
            complete(HttpResponse(StatusCodes.OK))
          }
        }
      )
    }
}
