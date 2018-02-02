package org.enmichuk.jackson

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

object Deserialize extends App{

  case class Jobs(
    @JsonProperty("jobs-running") jobsRunning: List[String],
    @JsonProperty("jobs-finished") jobsFinished: List[String],
    @JsonProperty("jobs-cancelled") jobsCancelled: List[String],
    @JsonProperty("jobs-failed") jobsFailed: List[String]
  )

  val source = "{\"jobs-running\":[\"fa97319891a2a15d4033e653206a1b8d\"],\"jobs-finished\":[],\"jobs-cancelled\":[],\"jobs-failed\":[]}"

  val objectMapper = new ObjectMapper() with ScalaObjectMapper
  objectMapper.registerModule(DefaultScalaModule)
  val jobs = objectMapper.readValue[Jobs](source)
  println(jobs.jobsRunning.length)

}
