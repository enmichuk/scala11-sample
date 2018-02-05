package org.enmichuk.http4s.circe

import fs2.{Stream, Task}
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._
import org.http4s.server.blaze._
import org.http4s.util.StreamApp

object Server extends StreamApp {

  val service = HttpService {
    case req@POST -> Root / "hello" =>
      for {
      // Decode a User request
        user <- req.as(jsonOf[User])
        // Encode a hello response
        resp <- Ok(List(Hello(user.name)).asJson)
      } yield resp
  }


  override def stream(args: List[String]): Stream[Task, Nothing] = BlazeBuilder
    .bindHttp(8080, "localhost").mountService(service, "/").serve

}

case class User(name: String)
case class Hello(greeting: String)