package pl.edu.pw.elka.stud.tkogut.passim.search
import java.util.Date
import java.net.URL

/**
 * @author tomek
 * 
 */

class SingleSearchResult(url: URL, title: String, description: String) {
  val mURL = url
  val mDescription = description
  val mTitle = title
  val mAdditionalAttributes = scala.collection.mutable.Map[String, String]()

  def this(url: String, title: String, description: String) = this(new URL(url), title, description)

  override def toString() = {
    val sb: StringBuilder = new StringBuilder
    sb append "Web result"
    sb append "\n\tURL: "
    sb append url
    sb append "\n\tTitle: "
    sb append title
    sb append "\n\tDescription: "
    sb append description
    sb.toString()
  }
}

object SingleSearchResult {
  val MISSING_DESCRIPTION: String = "missing_description"
  val MISSING_TITLE: String = "missing_title"
}