package digger.elasticsearch

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.{Delete, Get, Head, Post, Put}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import digger.elasticsearch.Protocol._
import spray.json._

import scala.concurrent.Future

class EsClient(val configuration: EsConfiguration = EsDefaultConfiguration)(implicit system: ActorSystem) {
  private implicit val executionContext = system.dispatcher
  private val http = Http()

  def aliases(): Future[Map[String, Aliases]] =
    call[Map[String, Aliases]](Get(serviceUrl("_aliases")))

  def stats(selector: String): Future[Stats] =
    call[Stats](Get(serviceUrl(s"$selector/_stats/docs,store")))

  def index(selector: String): Future[Map[String, IndexState]] =
    call[Map[String, IndexState]](Get(serviceUrl(selector)))

  def indexExists(name: String): Future[Boolean] =
    callSuccess(Head(serviceUrl(name)))

  def deleteIndex(name: String): Future[Boolean] =
    callSuccess(Delete(serviceUrl(name)))

  def createIndex(name: String, settings: Option[IndexSettings] = None) =
    callSuccess(
      Put(
        serviceUrl(name),
        content = Some(IndexState(Map(), Mappings(), IndexStateSettings(settings.getOrElse(IndexSettings()))))
      )
    )

  def indexDocument(indexName: String, document: JsObject): Future[Boolean] = {
    callSuccess(Post(serviceUrl(s"$indexName/_doc"), Some(document)))
  }

  def clusterHealth(level: ClusterHealthLevel.Value = ClusterHealthLevel.Cluster): Future[ClusterHealth] =
    call[ClusterHealth](
      Get(
        serviceUrl("_cluster/health")
          .withQuery(Query("level" -> level.toString))
      )
    )

  private def call[T](request: HttpRequest)(implicit um: Unmarshaller[ResponseEntity, T]): Future[T] =
    for {
      response <- http.singleRequest(request)
      entity <- Unmarshal(response.entity).to[T]
    } yield entity

  private def callSuccess(request: HttpRequest): Future[Boolean] =
    http
      .singleRequest(request)
      .map { response =>
        response.discardEntityBytes()
        response.status match {
          case _: StatusCodes.Success => true
          case _                      => false
        }
      }

  private def serviceUrl(relativeUrl: String): Uri =
    Uri.parseAndResolve(
      relativeUrl,
      base = Uri.parseAbsolute(configuration.serviceUrl)
    )
}
