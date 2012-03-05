package pl.edu.pw.elka.stud.tkogut.passim.websearchers

import pl.edu.pw.elka.stud.tkogut.passim.sade.core.yellowpages._
import pl.edu.pw.elka.stud.tkogut.passim.search.GoogleSearchSingleResult
import pl.edu.pw.elka.stud.tkogut.passim.search.GoogleSearch
import pl.edu.pw.elka.stud.tkogut.passim.brokering.SearchAgent
import pl.edu.pw.elka.stud.tkogut.passim.brokering.messages._
import java.io.IOException

class GoogleSearcherAgent(nameOfAgent: String) extends SearchAgent(nameOfAgent) {

  private val API_KEY = "AIzaSyCZ72XdhT4SOG2BUdGFA043jNxT9Fd4wPk"
  val googleGate = new GoogleSearch(API_KEY)

  override def uniqueName() = nameOfAgent
  override def processDialog(id: String) = {}
  def search(queryMsg: QueryMessage) = {
    val sr: SearchResultMessage = new SearchResultMessage(this, queryMsg.dialogId)
    var result: List[GoogleSearchSingleResult] = Nil;
    try {
      result = googleGate.search(queryMsg.query)
    } catch {
      case eio: IOException =>
        speak("Google did not provide search results:" + eio.toString)
        result = Nil
    } finally {
      sr.resultsList = result
    }
    activeDialogs(queryMsg.dialogId).contact ! sr
  }
}