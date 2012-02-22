package pl.edu.pw.elka.stud.tkogut.passim.agents.searchers
import pl.edu.pw.elka.stud.tkogut.passim.agents._
import pl.edu.pw.elka.stud.tkogut.passim.agents.yellowpages.YellowPagesAgent
import pl.edu.pw.elka.stud.tkogut.passim.search.GoogleSearchSingleResult
import pl.edu.pw.elka.stud.tkogut.passim.search.GoogleSearch
import pl.edu.pw.elka.stud.tkogut.passim.messages.QueryWeb
import pl.edu.pw.elka.stud.tkogut.passim.messages.SearchResultMessage
import java.io.IOException

class GoogleSearcherAgent(nameOfAgent: String) extends SearchAgent(nameOfAgent) {

  private val API_KEY = "AIzaSyCZ72XdhT4SOG2BUdGFA043jNxT9Fd4wPk"
  val googleGate = new GoogleSearch(API_KEY)

  override def uniqueName() = nameOfAgent
  override def processDialog(id: String) = {}
  def search(query: QueryWeb) = {
    val sr: SearchResultMessage = SearchResultMessage(query.dialogId)
    var result: List[GoogleSearchSingleResult] = Nil;
    try {
      result = googleGate.search(query.q)
    } catch {
      case eio: IOException =>
        speak("Google did not provide search results")
        result = Nil
    } finally {
      sr.results = result
    }
    activeDialogs(query.dialogId).mContact ! sr
  }
}