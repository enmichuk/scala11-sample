package json4s

import json4s.SomeObject.SubscriberId
import org.json4s.Extraction.decompose
import org.json4s.JsonDSL._
import org.json4s.{CustomKeySerializer, DefaultFormats}

object TypeAliasSerialization extends App {
  implicit val fmts = DefaultFormats + new CustomKeySerializer[SubscriberId](
    _ => ( {
      case s: String => s.toLong
    }, {
      case k: SubscriberId => k.toString
    }
    ))
  type Alias2 = Long

  case class C1(f: SubscriberId, a: String)

  case class C2(f: List[Alias2])

  val c = C1(1, "b")
  println(("f" -> c.f) ~ ("a" -> c.a))
  println(decompose(C2(List(1))))
  println(decompose(c))
}

object SomeObject {
  type SubscriberId = Long
}
