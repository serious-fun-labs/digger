package digger.web

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

class Api(implicit val actorSystem: ActorSystem) {
  private implicit val executionContext = actorSystem.dispatcher

  val route = pathPrefix("api") {
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
}
