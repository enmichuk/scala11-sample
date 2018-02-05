package org.enmichuk.http4s.json4s

import org.http4s.json4s.native.{jsonEncoderOf, jsonOf}
import org.json4s.JsonDSL._
import org.json4s.{DefaultFormats, JValue, Reader, Writer}
import Codec._

object Codec {
  type SubscriberId = Long

  implicit val formats = DefaultFormats
  implicit val somethingWriter = new Writer[Something] {
    override def write(obj: Something): JValue = ("body" -> obj.body) ~ ("set" -> obj.set)
  }
  implicit val somethingEncoder = jsonEncoderOf[Something]
  implicit val somethingReader = new Reader[Something] {
    override def read(value: JValue): Something = {
      val body = (value \ "body").extract[String]
      val set = (value \ "set").extract[Set[SubscriberId]]
      Something(body, set)
    }
  }
  implicit val somethingDecoder = jsonOf[Something]

}

case class Something(body: String, set: Set[SubscriberId], long: Long = 1505487229074L)