package digger.elasticsearch

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.Get
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import digger.elasticsearch.Protocol._

import scala.concurrent.Future

class EsClient(
    val configuration: EsConfiguration = EsDefaultConfiguration
)(implicit
    system: ActorSystem
) {
  implicit val executionContext = system.dispatcher

  def aliases(): Future[Map[String, Aliases]] =
    call[Map[String, Aliases]](Get(serviceUrl("_aliases")))

  def index(selector: String): Future[Map[String, IndexState]] =
    call[Map[String, IndexState]](Get(serviceUrl(selector)))

  def clusterHealth(
      level: ClusterHealthLevel.Value = ClusterHealthLevel.Cluster
  ): Future[ClusterHealth] =
    call[ClusterHealth](
      Get(
        serviceUrl("_cluster/health")
          .withQuery(Query("level" -> level.toString))
      )
    )

  private def call[T](
      request: HttpRequest
  )(implicit
      um: Unmarshaller[ResponseEntity, T]
  ): Future[T] =
    for {
      response <- Http().singleRequest(request)
      entity <- Unmarshal(response.entity).to[T]
    } yield entity

  private def serviceUrl(relativeUrl: String): Uri =
    Uri.parseAndResolve(
      relativeUrl,
      base = Uri.parseAbsolute(configuration.serviceUrl)
    )
}
