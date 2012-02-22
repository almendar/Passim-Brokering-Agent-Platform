package pl.edu.pw.elka.stud.tkogut.passim.agents.searchers

import pl.edu.pw.elka.stud.tkogut.passim.agents._
import pl.edu.pw.elka.stud.tkogut.passim.search._
import pl.edu.pw.elka.stud.tkogut.passim.agents.yellowpages.YellowPagesAgent
import org.scalatest.BeforeAndAfter
import pl.edu.pw.elka.stud.tkogut.passim.messages._

class BingSearcherAgent(nameofAgent: String) extends SearchAgent(nameofAgent) {

  val bingGate = new BingSearch("8A4C8362BAF8F435BCF3F8854CBEF493006E398A")

  override def uniqueName() = nameofAgent

  override def processDialog(id: String) {}

  def search(query: QueryWeb) = {
    val sr: SearchResultMessage = SearchResultMessage(query.dialogId)
    sr.results = bingGate.search(query.q)
    activeDialogs(query.dialogId).mContact ! sr
  }
}