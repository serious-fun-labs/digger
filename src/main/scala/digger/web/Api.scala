package digger.web

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import digger.elasticsearch.EsClient
import digger.web.ApiProtocol._

import scala.concurrent.{ExecutionContextExecutor, Future}

class Api(
    implicit val actorSystem: ActorSystem,
    implicit val esClient: EsClient
) extends ApiBase
    with SearchApiPart
    with ElasticsearchApiPart {
  override implicit val executionContext: ExecutionContextExecutor =
    actorSystem.dispatcher

  val route: Route =
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
