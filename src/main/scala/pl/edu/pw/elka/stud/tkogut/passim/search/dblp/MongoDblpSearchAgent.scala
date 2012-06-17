package pl.edu.pw.elka.stud.tkogut.passim.search.dblp

import pl.edu.pw.elka.stud.tkogut.brokering.SearchAgent
import pl.edu.pw.elka.stud.tkogut.brokering.messages._
import pl.edu.pw.elka.stud.tkogut.brokering.tools._

//case class MongoDblpSingleResult

class MongoDblpSearchAgent(name: String) extends SearchAgent(name) {

  /**
   * @TODO Capabilities to check and reuse
   *
   */
  override final val capabilities = List()

  val dblpGate = new MongoDblpGate()
  dblpGate.connect()

  def search(queryMsg: QueryMessage) = {
    val sr = new SearchResultMessage(this, queryMsg.dialogId)
    val queryText = queryMsg.query
    // val authorsList = dblpGate.lookForAuthors(queryText).limit(30)
    val documentsList = dblpGate.lookForPublications(queryText).limit(500)
    sr.resultsList = documentsList.seq.map {
      ae =>
        val title = if (ae.containsField("title")) ae.get("title").toString else SingleSearchResult.MISSING_TITLE
        val desc = SingleSearchResult.MISSING_DESCRIPTION

        var result: SingleSearchResult = null
        val url = if (ae.containsField("url"))
          result = new SingleWebSearchResult("http://dblp.uni-trier.de/" + ae.get("url"), title, desc)
        else
          result = new SingleSearchResult(title, desc)

        if (ae.containsField("author")) result.additionalAttributes += ("Author" -> ae.get("author").toString)

        result.additionalAttributes += ("Dblp" -> "Knowledge base")

        result
    }.toList
    dialogMgr.getContact(queryMsg.dialogId) ! sr
  }
}