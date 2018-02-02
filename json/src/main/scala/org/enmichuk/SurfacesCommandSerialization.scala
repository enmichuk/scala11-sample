package org.enmichuk

import java.io._
import java.nio.charset.StandardCharsets

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import io.circe.generic.JsonCodec
import io.circe.generic.extras.auto._
import io.circe.parser._
import io.circe.syntax._
import org.json4s.FullTypeHints
import org.json4s.native.Serialization

object SurfacesCommandSerialization extends App {

  var objectBytes: Array[Byte] = SurfacesCommandSerDe.serialize(UpdateSurfaceCommand("a"))
  var obj: Command = SurfacesCommandSerDe.deserialize(objectBytes)
  println(obj)
  objectBytes = SurfacesCommandSerDeJson4s.serialize(UpdateAllSurfacesCommand)
  obj = SurfacesCommandSerDeJson4s.deserialize(objectBytes)
  println(s"Json4s $obj")
  objectBytes = SurfacesCommandSerDeJson4s.serialize(UpdateSurfaceCommand("a"))
  obj = SurfacesCommandSerDeJson4s.deserialize(objectBytes)
  println(s"Json4s $obj")
  objectBytes = SurfacesCommandSerDeCirce.serialize(UpdateSurfaceCommand("df"))
  obj = SurfacesCommandSerDeCirce.deserialize(objectBytes)
  println(s"Circe $obj")
  val surfacesCommandSerDeJackson = new SurfacesCommandSerDeJackson()
  objectBytes = surfacesCommandSerDeJackson.serialize(UpdateSurfaceCommand1("df"))
  println(new String(objectBytes))
  val obj1 = surfacesCommandSerDeJackson.deserialize(objectBytes)
  println(obj1)

}

object SurfacesCommandSerDe {

  def serialize(command: Command): Array[Byte] = {
    val baos = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(baos)
    oos.writeObject(command)
    oos.close()
    baos.toByteArray
  }

  def deserialize(objectBytes: Array[Byte]): Command = {
    val ois = new ObjectInputStream(new ByteArrayInputStream(objectBytes))
    val command = ois.readObject.asInstanceOf[Command]
    ois.close()
    command
  }
}

object SurfacesCommandSerDeJson4s {
  import org.json4s.native.JsonMethods.{compact, parse, render}
  import org.json4s.{Extraction, Formats}

  implicit val formats: Formats =
    Serialization.formats(FullTypeHints(List(classOf[Command])))

  def serialize(command: Command): Array[Byte] = {
    compact(render(Extraction.decompose(command))).getBytes(StandardCharsets.UTF_8)
  }

  def deserialize(jsonBytes: Array[Byte]): Command = {
    parse(new String(jsonBytes, StandardCharsets.UTF_8)).extract[Command]
  }
}

object SurfacesCommandSerDeCirce {
  def serialize(command: Command): Array[Byte] = {
    command.asJson.noSpaces.getBytes(StandardCharsets.UTF_8)
  }

  def deserialize(jsonBytes: Array[Byte]): Command = {
    val json = new String(jsonBytes, StandardCharsets.UTF_8)
    decode[Command](json) match {
      case Left(error) => CorruptedCommand(s"[$json]", error.getMessage)
      case Right(figure) => figure
    }
  }
}

class SurfacesCommandSerDeJackson {
  private val mapper = new ObjectMapper() with ScalaObjectMapper
  locally {
    mapper.registerModule(DefaultScalaModule)
  }

  def serialize(command: UpdateSurfaceCommand1): Array[Byte] = {
    JsonUtil.toJson(command).getBytes(StandardCharsets.UTF_8)
  }

  def deserialize(jsonBytes: Array[Byte]): UpdateSurfaceCommand1 = {
    JsonUtil.fromJson[UpdateSurfaceCommand1](new String(jsonBytes, StandardCharsets.UTF_8))
  }
}

@JsonCodec sealed trait Command

case class CorruptedCommand(source: String, errorMessage: String) extends Command

case class CommandSerializationException(message: String) extends Exception(message)

case class UpdateSurfaceCommand(a: String) extends Command
case class UpdateSurfaceCommand1(a: String) {
  def this() {
    this("")
  }
}
case object UpdateAllSurfacesCommand extends Command

object Command
