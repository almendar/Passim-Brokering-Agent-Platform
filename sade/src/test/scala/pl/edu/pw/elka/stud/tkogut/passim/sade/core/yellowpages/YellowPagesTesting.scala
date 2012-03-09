package pl.edu.pw.elka.stud.tkogut.passim.sade.core.yellowpages
import org.scalatest._
import pl.edu.pw.elka.stud.tkogut.passim.sade.core._
import pl.edu.pw.elka.stud.tkogut.passim.sade.core.yellowpages._
import pl.edu.pw.elka.stud.tkogut.passim.sade.messages.Message

class YellowPagesMessages(msgText: String) extends Message

class YellowPagesTesting extends FunSuite with BeforeAndAfterEach with BeforeAndAfter {

  val MESSAGE_REGISTER_YOURSELF = "REGISTER"
  val MESSAGE_OK = "OK";

  before {
    println("YELLOW1");
    YellowPagesAgent.start()

  }

  after {
    //YellowPagesAgent.exit();
  }

  test("Registration test") {

    val a = new Agent("Agent1") with YelloPagesSearchable {
      override def handleMessage(msg: Message) = {
        msg match {
          case q:YellowPagesMessages => null
        }
      }

      override def processDialog(id: String) = {}

      override def uniqueName() = "Agent1"

    }

  }
}
