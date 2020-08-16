name := "digger"

version := "0.1"

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.1.11",
  "com.typesafe.akka" %% "akka-stream" % "2.6.4",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.11"
)
