package org.enmichuk.core

object StructuralTypes extends App {

  case class Bar(bar: String)

  def foo(a: {val bar: String}) = {
    println(a.bar)
  }

  foo(new {
    val bar = "bar"
  })
  foo(Bar("bar"))

}
