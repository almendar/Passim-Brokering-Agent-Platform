package pl.edu.pw.elka.stud.tkogut.passim.search
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfterEach
import pl.edu.pw.elka.stud.tkogut.sade.core._
import pl.edu.pw.elka.stud.tkogut.sade.core.yellowpages._
import pl.edu.pw.elka.stud.tkogut.sade.messages._
import pl.edu.pw.elka.stud.tkogut.brokering._
import pl.edu.pw.elka.stud.tkogut.brokering.messages._
import pl.edu.pw.elka.stud.tkogut.passim.search.google._
import pl.edu.pw.elka.stud.tkogut.passim.search.bing._
import pl.edu.pw.elka.stud.tkogut.brokering.tools._

import java.net.URL
import java.util.Calendar

class AgentCommunicationTest extends FunSuite with BeforeAndAfterEach {

  val BROKER_NAME = "BrokerAgent"
  val GOOGLE_NAME = "GoogleSearch"
  val BING_NAME = "BingSearch"
  val QUERY_TEXT = "Politechnika Warszawska"
  val names = List(GOOGLE_NAME, BING_NAME)

  val queryRes1URL = new URL("http://www.pw.edu.pl")
  val queryRes1Title = "Politechnika Warszawska"
  val queryRes1Desc = "Najstarsza uczelnia w Polsce"
  val queryRes1Date = Calendar.getInstance()
  queryRes1Date.set(2012, 1, 1, 12, 0)

  val queryRes2URL = new URL("http://www.pw.edu.pl")
  val queryRes2Title = "Instytut informatyki"
  val queryRes2Desc = "Wydział informatyki"

  val queryRes3URL = new URL("http://www.ia.pw.edu.pl")
  val queryRes3Title = "Instytut automatyki"
  val queryRes3Desc = "Wydział automatyki"
  val queryRes3Date = Calendar.getInstance()
  queryRes3Date.set(2011, 2, 2, 13, 1)

  override def beforeEach() {
    YellowPagesAgent.start
  }

  test("Searching in internet") {

    val gs = new GoogleSearcherAgent(GOOGLE_NAME) {
      override def search(queryMsg: QueryMessage) {
        val sr: SearchResultMessage = new SearchResultMessage(this, queryMsg.dialogId)
        var result = List[GoogleSearchSingleResult](
          new GoogleSearchSingleResult(queryRes1URL, queryRes1Title, queryRes1Desc),
          new GoogleSearchSingleResult(queryRes2URL, queryRes2Title, queryRes2Desc))
        sr.resultsList = result
        dialogMgr.getContact(queryMsg.dialogId) ! sr
      }
    }

    val bs = new BingSearcherAgent(BING_NAME) {
      override def search(queryMsg: QueryMessage) {
        val sr: SearchResultMessage = new SearchResultMessage(this, queryMsg.dialogId)

        val result = List[BingSearchSingleResult](new BingSearchSingleResult(queryRes1URL, queryRes1Title, queryRes1Desc, queryRes1Date.getTime()),
          new BingSearchSingleResult(queryRes3URL, queryRes3Title, queryRes3Desc, queryRes3Date.getTime()))
        sr.resultsList = result
        dialogMgr.getContact(queryMsg.dialogId) ! sr

      }

    }

    gs.start(); bs.start()

    val ba = new BrokerAgent(BROKER_NAME)
    ba.start()

    Thread.sleep(300)

    val talker = new Agent("Controller") {

      override def act() = {
        establishDialog(ba)
        super.act();
      }

      override def handleMessage(msg: Message) {
        msg match {
          case x: SearchResultMessage =>
            val result = x.resultsList.asInstanceOf[List[SingleWebSearchResult]]
            assert(result.length == 3);
            result.foreach { res: SingleWebSearchResult =>
              res.mURL match {
                case queryRes1URL =>
                  assert(res.additionalAttributes(BingSearchSingleResult.DATE_TIME_ATTRIBUTE) ==
                    queryRes1Date.toString)
                //case queryRes2URL =>
                  //assert(res.mAdditionalAttributes.isEmpty)
              }

            }
        }
      }

      override def processDialog(id: String) {
        dialogMgr.getContact(id) ! (QueryMessage(QUERY_TEXT, id))
      }
    }
    talker.start()
  }

}