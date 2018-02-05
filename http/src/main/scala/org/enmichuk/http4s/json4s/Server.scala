package org.enmichuk.http4s.json4s

import fs2.{Stream, Task}
import org.http4s._
import org.http4s.dsl._
import org.http4s.server.blaze._
import org.http4s.util.StreamApp

import scala.util.{Failure, Success, Try}

object Server extends StreamApp {
  import Codec._

  val service = HttpService {
    case GET -> Root / "hello" / name =>
      Ok(s"Hello, $name.")
    case GET -> Root / "object" =>
      Try(Ok(Something("abc", Set(1)))) match {
        case Success(ok) => ok
        case Failure(e) =>
          e.printStackTrace()
          InternalServerError(e.getMessage)
      }
    case req@POST -> Root / "object" =>
      req.decode[Something] { data =>
        println(data)
        Ok(data)
      }
  }

  override def stream(args: List[String]): Stream[Task, Nothing] = BlazeBuilder
    .bindHttp(8080, "localhost").mountService(service, "/").serve
}