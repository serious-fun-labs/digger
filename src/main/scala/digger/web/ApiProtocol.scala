package digger.web

import spray.json._

case class ElasticsearchConfiguration(serviceUrl: String)

case class ElasticsearchIndexStatus(name: String, status: String)

case class ElasticsearchClusterStatus(
    name: String,
    status: String,
    numberOfNodes: Int,
    indices: Seq[ElasticsearchIndexStatus]
)

case class ElasticsearchStatus(
    configuration: ElasticsearchConfiguration,
    cluster: Option[ElasticsearchClusterStatus] = None,
    error: Option[String] = None
)

object ApiProtocol extends DefaultJsonProtocol {
  implicit val elasticsearchConfigurationFormat = jsonFormat1(
    ElasticsearchConfiguration
  )
  implicit val elasticsearchIndexStatusFormat = jsonFormat2(
    ElasticsearchIndexStatus
  )
  implicit val elasticsearchOnlineStatusFormat = jsonFormat4(
    ElasticsearchClusterStatus
  )
  implicit val elasticsearchStatusFormat = jsonFormat3(ElasticsearchStatus)
}
