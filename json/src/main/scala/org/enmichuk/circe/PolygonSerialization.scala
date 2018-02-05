package org.enmichuk.circe

import io.circe.generic.JsonCodec
import io.circe.generic.extras.auto._
import io.circe.parser._
import io.circe.syntax._

object PolygonSerialization extends App {
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