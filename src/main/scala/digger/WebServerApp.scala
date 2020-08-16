package digger

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.concat
import com.typesafe.config.{Config, ConfigFactory}
import digger.web._

import scala.io.StdIn

object WebServerApp extends App {
  implicit val system = ActorSystem("my-system", akkaConfig)
  implicit val executionContext = system.dispatcher

  val api = new Api()
  val pages = new Pages()

  val route = concat(api.route, pages.route)

  val bindingFuture = Http().bindAndHandle(route, "localhost", 5000)

  println(s"Server running at http://localhost:5000/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done

  private def akkaConfig: Config = {
    // make a Config with just your special setting
    val myConfig =
      ConfigFactory.parseString("akka.http.client.parsing.max-chunk-size = 20m")
    // load the normal config stack (system props,
    // then application.conf, then reference.conf)
    val regularConfig = ConfigFactory.load()
    // override regular stack with myConfig
    val combined = myConfig.withFallback(regularConfig)
    // put the result in between the overrides
    // (system props) and defaults again
    ConfigFactory.load(combined)
  }
}
