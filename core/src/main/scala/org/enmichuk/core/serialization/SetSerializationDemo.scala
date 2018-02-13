package org.enmichuk.core.serialization

import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

object SetSerializationDemo extends App {

  val FileName = ""

  val obj = Set[SubscriberId]()

  // (2) write the instance out to a file
  val oos = new ObjectOutputStream(new FileOutputStream(FileName))
  oos.writeObject(obj)
  oos.close

  // (3) read the object back in
  val ois = new ObjectInputStream(new FileInputStream(FileName))
  val stock = ois.readObject.asInstanceOf[Set[SubscriberId]]
  ois.close

  // (4) print the object that was read back in
  println(stock)

}

trait SubscriberId
