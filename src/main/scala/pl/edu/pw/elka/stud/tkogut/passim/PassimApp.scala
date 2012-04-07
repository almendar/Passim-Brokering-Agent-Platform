package pl.edu.pw.elka.stud.tkogut.passim
import java.io.File
import pl.edu.pw.elka.stud.tkogut.brokering.dialect._


object PassimApp {

  def main(args: Array[String]): Unit = {
    val d = Dialect("BrokerDialect")
    val e = Entity("Person")
    e.attributes ++= List(
      Attribute("Name", AttributeType.STRING),
      Attribute("Surname", AttributeType.STRING),
      Attribute("Age", AttributeType.INTEGER))
    d += e

    println(d)
  }
}