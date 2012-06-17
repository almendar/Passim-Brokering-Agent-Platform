package pl.edu.pw.elka.stud.tkogut.sade.core.yellowpages

import org.scalatest._
import pl.edu.pw.elka.stud.tkogut.sade.messages._
import pl.edu.pw.elka.stud.tkogut.sade.core.Agent;

case class YellowPagesTestMessages(msgText: String) extends Message

class YellowPagesTesting extends FunSuite with BeforeAndAfterEach with BeforeAndAfter {

  val MESSAGE_REGISTER_YOURSELF = "REGISTER"
  val MESSAGE_OK = "OK";
  var helpAgent: Agent = null

  before {

    YellowPagesAgent.start()

  }

  after {

  }


  test("Registration test") {
    val AGENT_NAME: String = "John"
    val agent = new Agent(AGENT_NAME) with YellowPagesSearchable {
      override def handleMessage(msg: Message) = {}

      override def processDialog(id: String) = {}
    }
    var registered = false
    var counter = 10
    while (counter > 0) {
      registered = YellowPagesAgent.book.contains(agent)
      counter -= 1
      if (registered)
        counter = 0
      Thread.sleep(50)
    }
    assert(registered)
    YellowPagesAgent.getNamesOfRegisteredAgents().contains(AGENT_NAME)
  }

  test("FilteringTest") {
    new Agent("John") with YellowPagesSearchable {
      override def handleMessage(msg: Message) = {}

      override def processDialog(id: String) = {}
    }
    new Agent("Albert") {
      override def handleMessage(msg: Message) = {}

      override def processDialog(id: String) = {}

      override def act() = {
        YellowPagesAgent ! new SendAgentsMeetingConstraint(this, (a: Agent) => a.name == "John")
        receive {
          case l: AgentListQueryMessage => {
            //speak("Got list of Agents")
            assert(l.getAgents.toList.head.name == "John")
          }

        }
      }
    }.start
  }
}
