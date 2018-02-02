package org.enmichuk.circe

import io.circe.generic.extras.auto._
import io.circe._
import io.circe.generic.JsonCodec
import io.circe.syntax._
import io.circe.parser._

object PolygonSerialization extends App {

  //  implicit val encoder = Encoder.instance[Geometry] {
  //    case p: Polygon => Json.obj((p.getClass.getSimpleName, p.asJson))
  //    case m: MultiPolygon => Json.obj((m.getClass.getSimpleName, m.asJson))
  //    case _ => throw new Exception("Unsupported object type")
  //  }
  //
  //  implicit val decoder = Decoder.instance[Geometry] {
  //    a =>
  //      if (a.downField("Polygon").succeeded) a.downField("Polygon").as[Polygon]
  //      else if (a.downField("MultiPolygon").succeeded) a.downField("MultiPolygon").as[MultiPolygon]
  //      else throw new Exception("")
  //  }
  //
  val p: Geometry = Polygon(List(List(Coord(0, 0), Coord(1, 0), Coord(1, 1))))
  val json = p.asJson.noSpaces
  println(json)

  val s = ""

  val decoded = decode[Geometry](json) match {
    case Left(error) => CorruptedPolygon(s"[$json]")
    case Right(figure) => figure
  }
  println(decoded)

}

@JsonCodec case class Coord(x: Double, y: Double)

@JsonCodec sealed trait Geometry

case class Polygon(coordinates: List[List[Coord]]) extends Geometry

case class MultiPolygon(coordinates: List[List[List[Coord]]]) extends Geometry

case class CorruptedPolygon(source: String) extends Geometry

object Geometry