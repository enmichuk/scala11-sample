package org.enmichuk.http4s.circe

import io.circe.syntax._
import fs2.Task
import io.circe.generic.auto._
import org.http4s.Request
import org.http4s.circe._
import org.http4s.client.blaze._
import org.http4s.dsl._

object Client extends App {

  val httpClient = PooledHttp1Client()
  // Decode the Hello response
  def helloClient(name: String): Task[List[Hello]] = {
    // Encode a User request
    val req = Request(POST, uri("http://localhost:8080/hello")).withBody(User(name).asJson)
    // Decode a Hello response
    httpClient.expect(req)(jsonOf[List[Hello]])
  }

  val helloAlice = helloClient("Alice")

  println(helloAlice.unsafeRun)

}