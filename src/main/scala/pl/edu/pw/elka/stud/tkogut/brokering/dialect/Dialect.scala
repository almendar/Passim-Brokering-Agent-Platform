package pl.edu.pw.elka.stud.tkogut.brokering.dialect
import scala.collection.mutable.ArrayBuffer

case class Dialect(dialectName: String)  extends ArrayBuffer[Entity] {
  
  
  
  override def toString = {
    val sb = new StringBuilder
    sb ++= dialectName + "\n{\n"
    for (i <- this) {
      sb ++= "\t" + i + "\n"
    }
    sb ++= "\n}"
    sb.mkString
  }
 

  
}







