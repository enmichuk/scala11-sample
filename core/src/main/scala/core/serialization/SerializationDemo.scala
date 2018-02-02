package core.serialization

import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._


object SerializationDemo extends App {

  val FileName = ""

  // (1) create a Stock instance
  //  val obj = new Stock("NFLX", BigDecimal(85.00))
  //  val obj = Trait(1)
  val obj = MockitoSugar.mock[Trait](withSettings().serializable())

  // (2) write the instance out to a file
  val oos = new ObjectOutputStream(new FileOutputStream(FileName))
  oos.writeObject(obj)
  oos.close

  // (3) read the object back in
  val ois = new ObjectInputStream(new FileInputStream(FileName))
  val stock = ois.readObject.asInstanceOf[Trait]
  ois.close

  // (4) print the object that was read back in
  println(stock)

}

class Stock(var symbol: String, var price: BigDecimal) extends Serializable {
  override def toString = f"$symbol%s is ${price.toDouble}%.2f"
}

trait Trait {
  def field: Int
}

object Trait {
  def apply(i: Int): Trait = {
    TraitImpl(i)
  }

  def unapply(target: Trait): Option[Int] = Option(target.field)
}

private case class TraitImpl(field: Int) extends Trait
