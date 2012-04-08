package pl.edu.pw.elka.stud.tkogut.brokering.tools
import scala.collection.mutable.Map

class SingleSearchResult(title: String, description: String) {

  protected def openTag = "Result"

  val _additionalAttributes = Map[String, String]()
  val mDescription = description
  val mTitle = title

  def additionalAttributes = _additionalAttributes

  override def toString() = {
    val sb: StringBuilder = new StringBuilder
    sb append openTag
    sb append "\n\tTitle: "
    sb append title
    sb append "\n\tDescription: "
    sb append description
    for ((k, v) <- additionalAttributes) {
      sb append ("\n\t" + k + ": ")
      sb append v
    }
    sb.toString()
  }
}

object SingleSearchResult {
  val MISSING_DESCRIPTION: String = "NO DESCRIPTION"
  val MISSING_TITLE: String = "NO TITLE"
}