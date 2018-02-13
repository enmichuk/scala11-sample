package org.enmichuk.core.monad.trymonad

import scala.util.Try

object RecoverTryExample extends App {

  val try1 = Try[String] {
    throw new Exception("failed")
  }.toOption

  println(try1)

  val try2 = Try[String] {
    throw new Exception("failed")
  }.recover {
    case e => throw e
  }.toOption

  println(try2)

}
