package digger.elasticsearch

import digger.json.EnumerationJsonFormat
import spray.json._

case class Aliases(aliases: Map[String, JsObject])

case class ShardsStats(total: Int, successful: Int, failed: Int)

case class DocsStats(count: Long = 0, deleted: Long = 0)

case class StoreStats(sizeInBytes: Long = 0)

case class IndexStats(docs: Option[DocsStats], store: Option[StoreStats])

case class StatsAggs(primaries: IndexStats, total: IndexStats)

case class IndexStatsAggs(uuid: String, primaries: IndexStats, total: IndexStats)

case class Stats(shards: ShardsStats, all: StatsAggs, indices: Map[String, IndexStatsAggs])

case class Field(`type`: String, ignoreAbove: Option[Int] = None)

case class Property(`type`: String, fields: Option[Map[String, Field]] = None)

case class Mappings(properties: Option[Map[String, Property]] = None)

case class IndexSettings(
    numberOfShards: Option[String] = None,
    numberOfReplicas: Option[String] = None
)

case class IndexStateSettings(index: IndexSettings)

case class IndexState(
    aliases: Map[String, JsObject],
    mappings: Mappings,
    settings: IndexStateSettings
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

case class IndexHealth(status: HealthStatus.Value)

case class ClusterHealth(
    name: String,
    status: HealthStatus.Value,
    numberOfNodes: Int,
    indices: Option[Map[String, IndexHealth]]
)

object Protocol extends DefaultJsonProtocol {
  implicit val aliasesFormat = jsonFormat(Aliases, "aliases")
  implicit val shardsStatsFormat = jsonFormat(ShardsStats, "total", "successful", "failed")
  implicit val docsStatsFormat = jsonFormat(DocsStats, "count", "deleted")
  implicit val storeStatsFormat = jsonFormat(StoreStats, "size_in_bytes")
  implicit val indexStatsFormat = jsonFormat(IndexStats, "docs", "store")
  implicit val statsAggsFormat = jsonFormat(StatsAggs, "primaries", "total")
  implicit val indexStatsAggsFormat = jsonFormat(IndexStatsAggs, "uuid", "primaries", "total")
  implicit val statsFormat = jsonFormat(Stats, "_shards", "_all", "indices")
  implicit val fieldFormat = jsonFormat(Field, "type", "ignore_above")
  implicit val propertyFormat = jsonFormat(Property, "type", "fields")
  implicit val mappingsFormat = jsonFormat(Mappings, "properties")
  implicit val indexSettingsFormate = jsonFormat(IndexSettings, "number_of_shards", "number_of_replicas")
  implicit val indexStateSettingsFormat = jsonFormat(IndexStateSettings, "index")
  implicit val indexStateFormat = jsonFormat(IndexState, "aliases", "mappings", "settings")
  implicit val clusterStatusFormat = new EnumerationJsonFormat(HealthStatus)
  implicit val indexHealthFormat = jsonFormat(IndexHealth, "status")
  implicit val clusterHealthFormat =
    jsonFormat(ClusterHealth, "cluster_name", "status", "number_of_nodes", "indices")
}
