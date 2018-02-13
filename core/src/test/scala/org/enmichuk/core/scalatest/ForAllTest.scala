package org.enmichuk.core.scalatest

import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.Inspectors._

class ForAllTest extends FlatSpec with Matchers {
  "ForAll" should "work" in {
    val list = List(1, 2, 4, 5, 6)
    forAll(list)  {
      l => l should not equal 3
    }
  }
}
