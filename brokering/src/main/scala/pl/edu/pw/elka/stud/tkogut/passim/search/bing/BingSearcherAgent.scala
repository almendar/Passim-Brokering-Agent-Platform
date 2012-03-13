package pl.edu.pw.elka.stud.tkogut.passim.websearchers

import pl.edu.pw.elka.stud.tkogut.passim.brokering.SearchAgent
import pl.edu.pw.elka.stud.tkogut.passim.brokering.messages._
import pl.edu.pw.elka.stud.tkogut.passim.search.BingSearch

class BingSearcherAgent(nameofAgent: String) extends SearchAgent(nameofAgent) {

  val bingGate = new BingSearch("8A4C8362BAF8F435BCF3F8854CBEF493006E398A")

  override def processDialog(id: String) {}

  def search(query: QueryMessage) = {
    val sr: SearchResultMessage = new SearchResultMessage(this, query.dialogId)
    sr.resultsList = bingGate.search(query.query)
    activeDialogs(query.dialogId).contact ! sr
  }
}
