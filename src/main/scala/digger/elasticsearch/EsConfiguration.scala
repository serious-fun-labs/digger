package digger.elasticsearch

trait EsConfiguration {
  val serviceUrl: String
}

object EsDefaultConfiguration extends EsConfiguration {
  override val serviceUrl: String = "http://localhost:9200"
}
