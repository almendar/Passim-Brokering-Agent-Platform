package pl.edu.pw.elka.stud.tkogut.passim.brokering.messages
import pl.edu.pw.elka.stud.tkogut.passim.brokering.tools.SingleSearchResult
import pl.edu.pw.elka.stud.tkogut.passim.sade.core.Agent
import pl.edu.pw.elka.stud.tkogut.passim.sade.messages.DialogMessage

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