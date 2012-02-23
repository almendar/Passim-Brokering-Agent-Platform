package pl.edu.pw.elka.stud.tkogut.passim.messages
import pl.edu.pw.elka.stud.tkogut.passim.search.SingleSearchResult
import pl.edu.pw.elka.stud.tkogut.passim.agents.Agent

class SearchResultMessage(from: Agent, dialogID: String) extends DialogMessage(from, dialogID) {
  var resultsList: List[SingleSearchResult] = null

  override def toString() = {
    if (resultsList == null)
      super.toString()

    var i = 0;
    val sb: StringBuilder = new StringBuilder
    for (r <- resultsList) {
      i += 1
      sb append "\n\n" + i + " " + r.toString()
    }
    sb.mkString
  }
}