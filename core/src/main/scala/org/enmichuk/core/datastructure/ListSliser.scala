package org.enmichuk.core.datastructure

object ListSliser extends App {
  var list = List(1)
  var slised = list.sliding(2).toList
  println(slised)
  list = List(1, 2)
  slised = list.sliding(2).toList
  println(slised)
  list = List(1, 2, 3)
  slised = list.sliding(2).toList
  println(slised)
  list = List(1, 2, 3, 4)
  slised = list.sliding(2).toList
  println(slised)
  println(list.head)
  val reverted = list.foldRight[List[Int]](Nil) { case (element, l) =>
    element :: l
  }
  println(reverted)
  val revertedSliced = slised.foldRight[List[Int]](Nil) {
    case (List(_, tailElement), _) =>
      println(tailElement)
      Nil
  }
  println(Seq(1, 2) ++ Seq(3, 4))
}