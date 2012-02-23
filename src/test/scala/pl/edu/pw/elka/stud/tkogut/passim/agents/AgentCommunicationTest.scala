package pl.edu.pw.elka.stud.tkogut.passim.agents
import org.scalatest.FunSuite
import pl.edu.pw.elka.stud.tkogut.passim.agents.yellowpages.YellowPagesAgent
import org.scalatest.BeforeAndAfter
import pl.edu.pw.elka.stud.tkogut.passim.search.GoogleSearch
import pl.edu.pw.elka.stud.tkogut.passim.search.BingSearch
import pl.edu.pw.elka.stud.tkogut.passim.agents.searchers.GoogleSearcherAgent
import pl.edu.pw.elka.stud.tkogut.passim.agents.searchers.BingSearcherAgent
import pl.edu.pw.elka.stud.tkogut.passim.agents.brokering.BrokerAgent
import pl.edu.pw.elka.stud.tkogut.passim.messages.QueryMessage
import pl.edu.pw.elka.stud.tkogut.passim.messages.Message
import org.scalatest.BeforeAndAfterEach
import pl.edu.pw.elka.stud.tkogut.passim.messages.SearchResultMessage

class AgentRegistrationTest extends FunSuite with BeforeAndAfterEach {

  val BROKER_NAME = "BrokerAgent"
  val GOOGLE_NAME = "GoogleSearch"
  val BING_NAME = "BingSearch"
  val names = List(GOOGLE_NAME, BING_NAME)

  override def beforeEach() {
    YellowPagesAgent.book.clear()
    YellowPagesAgent.start
  }

  test("Agent registration test") {
    val agents: List[Agent] = List(new GoogleSearcherAgent(GOOGLE_NAME), new BingSearcherAgent(BING_NAME))
    agents.foreach(agent => agent.start())

    Thread.sleep(300)
    val listOfRegisteredNames = YellowPagesAgent.getNamesOfRegisteredAgents

    for (i <- names) {
      assert(listOfRegisteredNames.contains(i))
    }
  }

  test("Searching in internet") {
    val gs = new GoogleSearcherAgent(GOOGLE_NAME)
    val bs = new BingSearcherAgent(BING_NAME)

    gs.start(); bs.start()

    val ba = new BrokerAgent(BROKER_NAME)
    ba.start()

    Thread.sleep(300)

    val talker = new Agent("Controller") {

      override def handleMessage(msg: Message) {
        msg match {
          case (ba: Agent) => establishDialog(ba)
          case x: SearchResultMessage => assert(x.resultsList.length <= 20) //println(x)
        }
      }

      override def processDialog(id: String) {
        activeDialogs(id).contact ! QueryMessage("Politechnika Warszawska", id)
      }
    }
    talker.start()
    ba ! talker
  }

}