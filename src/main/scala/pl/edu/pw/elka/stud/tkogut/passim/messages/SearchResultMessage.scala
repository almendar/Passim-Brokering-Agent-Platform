package pl.edu.pw.elka.stud.tkogut.passim.messages
import pl.edu.pw.elka.stud.tkogut.passim.search.SingleSearchResult

case class SearchResultMessage(dialogID: String) extends DialogMessage(dialogID) {
  var results: List[SingleSearchResult] = null

  override def toString() = {
    if (results == null)
      super.toString()

    var i = 0;
    val sb: StringBuilder = new StringBuilder
    for (r <- results) {
      i += 1
      sb append "\n\n" + i + " " + r.toString()
    }
    sb.mkString
  }
}