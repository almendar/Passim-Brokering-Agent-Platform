package pl.edu.pw.elka.stud.tkogut.passim.search.bing

import pl.edu.pw.elka.stud.tkogut.brokering.SearchAgent
import pl.edu.pw.elka.stud.tkogut.brokering.messages._

class BingSearcherAgent(nameofAgent: String) extends SearchAgent(nameofAgent) {

  val bingGate = new BingSearch("8A4C8362BAF8F435BCF3F8854CBEF493006E398A")

  def search(query: QueryMessage) = {
    val sr: SearchResultMessage = new SearchResultMessage(this, query.dialogId)
    sr.resultsList = bingGate.search(query.query)
    activeDialogs(query.dialogId).contact ! sr
  }
}
