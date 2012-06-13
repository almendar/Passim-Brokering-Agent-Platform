package pl.edu.pw.elka.stud.tkogut.brokering.tools
import java.util.Date
import java.net.URL

/**
 * @author tomek
 *
 */

class SingleWebSearchResult(url: URL, title: String, description: String) extends SingleSearchResult(title,description) {
  def this(url: String, title: String, description: String) = this(new URL(url), title, description)
  val mURL = url

  override protected def openTag = "Web result"

  override def toString() : String = {
    val sb: StringBuilder = new StringBuilder
    sb append super.toString
    sb append "\n\tURL: "
    sb append mURL.toString
    sb.mkString
  }
}

