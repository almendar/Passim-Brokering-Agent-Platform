package pl.edu.pw.elka.stud.tkogut.brokering.messages
import pl.edu.pw.elka.stud.tkogut.brokering.tools.SingleSearchResult
import pl.edu.pw.elka.stud.tkogut.sade.messages.DialogMessage
import pl.edu.pw.elka.stud.tkogut.sade.core.Agent;

class SearchResultMessage(sender: Agent, dialogID: String) extends DialogMessage(sender, dialogID) {
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