package digger.web

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._

class Pages(implicit actorSystem: ActorSystem) {
  private implicit val executionContext = actorSystem.dispatcher

  val webAppPath = "./src/main/webapp/dist"
  val webAppPagePath = s"$webAppPath/index.html"

  val route =
    concat(
      pathSingleSlash {
        webAppPage
      },
      path("about") {
        webAppPage
      },
      getFromDirectory(webAppPath)
    )

  private def webAppPage =
    if (new File(webAppPagePath).isFile) {
      getFromFile(webAppPagePath)
    } else {
      complete("Please build webapp")
    }
}
