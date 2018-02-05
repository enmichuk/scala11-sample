package org.enmichuk.http4s.json4s

import fs2.Task
import org.http4s.client.blaze.PooledHttp1Client
import org.http4s.dsl._
import org.http4s.{Request, Uri}

object Client extends App {
  import Codec._

  val root = Uri.uri("http://localhost:8080")

  val httpClient = PooledHttp1Client()

  def hello(name: String): Task[String] = {
    httpClient.expect[String](root / "hello" / name)
  }
  def requestObject: Task[Something] = {
    httpClient.expect[Something](root / "object")
  }
  def sendObject(obj: Something): Task[String] = {
    val resource = root / "object"
    val req = Request(POST, resource).withBody(obj)
    httpClient.expect[String](req)
  }
  val people = Vector("Michael", "Jessica", "Ashley", "Christopher")

  val greetingList = Task.traverse(people)(hello)

  greetingList.unsafeAttemptRun() match {
    case Left(error) => error.printStackTrace()
    case Right(greetings) => println(greetings)
  }

  requestObject.unsafeAttemptRun() match {
    case Left(error) => error.printStackTrace()
    case Right(greetings) => println(greetings)
  }

  httpClient.shutdownNow()
}