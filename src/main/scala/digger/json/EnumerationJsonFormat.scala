package digger.json

import spray.json._

class EnumerationJsonFormat[T <: Enumeration](enu: T)
    extends RootJsonFormat[T#Value] {
  override def write(obj: T#Value): JsValue = JsString(obj.toString)

  override def read(json: JsValue): T#Value = {
    json match {
      case JsString(txt) => enu.withName(txt)
      case somethingElse =>
        throw DeserializationException(
          s"Expected a value from enum $enu instead of $somethingElse"
        )
    }
  }
}
