package digger.elasticsearch

import digger.json.EnumerationJsonFormat
import spray.json._

case class Aliases(aliases: Map[String, JsObject])

case class Field(`type`: String, ignoreAbove: Option[Int] = None)

case class Property(`type`: String, fields: Option[Map[String, Field]] = None)

case class Mappings(properties: Map[String, Property])

case class IndexState(
    aliases: Map[String, JsObject],
    mappings: Mappings
)

object ClusterHealthLevel extends Enumeration {
  val Cluster = Value("cluster")
  val Indices = Value("indices")
  val Shards = Value("shards")
}

object HealthStatus extends Enumeration {
  val Green = Value("green")
  val Yellow = Value("yellow")
  val Red = Value("red")
}

case class IndexStatus(status: HealthStatus.Value)

case class ClusterHealth(
    name: String,
    status: HealthStatus.Value,
    numberOfNodes: Int,
    indices: Option[Map[String, IndexStatus]]
)

object Protocol extends DefaultJsonProtocol {
  implicit val aliasesFormat = jsonFormat(Aliases, "aliases")
  implicit val fieldFormat = jsonFormat(Field, "type", "ignore_above")
  implicit val propertyFormat = jsonFormat(Property, "type", "fields")
  implicit val mappingsFormat = jsonFormat(Mappings, "properties")
  implicit val indexStateFormat = jsonFormat(IndexState, "aliases", "mappings")
  implicit val clusterStatusFormat = new EnumerationJsonFormat(
    HealthStatus
  )
  implicit val indexStatusFormat = jsonFormat(IndexStatus, "status")
  implicit val clusterHealthFormat =
    jsonFormat(
      ClusterHealth,
      "cluster_name",
      "status",
      "number_of_nodes",
      "indices"
    )
}
