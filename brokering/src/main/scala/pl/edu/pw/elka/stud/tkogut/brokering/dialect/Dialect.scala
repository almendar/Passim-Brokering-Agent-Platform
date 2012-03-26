package pl.edu.pw.elka.stud.tkogut.brokering.dialect
import scala.collection.mutable.HashSet

case class Dialect(dialectName: String) extends HashSet[Entity] {
  override def toString = {
    val sb = new StringBuilder
    sb ++= dialectName + " {"
    sb += '\n'
    for (i <- this) {
      sb ++= "\t" + i
    }
    sb ++= "\n}"
    sb.mkString
  }
}







