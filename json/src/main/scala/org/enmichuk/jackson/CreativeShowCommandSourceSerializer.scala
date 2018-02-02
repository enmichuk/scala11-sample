package org.enmichuk.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

object CreativeShowCommandSourceSerializer extends App {

  val objectMapper = new ObjectMapper() with ScalaObjectMapper
  objectMapper.registerModule(DefaultScalaModule)

  val commands = CreativeShowCommandsSource(List(CreativeShowCommandSource("a", "a", "a", Option(1))))

  println(objectMapper.writeValueAsString(commands))

}

case class CreativeShowCommandSource(
  Surface: String,
  Campaign: String,
  MediaItem: String,
  timestamp: Option[Int] = None
)

case class CreativeShowCommandsSource(commands: List[CreativeShowCommandSource])
