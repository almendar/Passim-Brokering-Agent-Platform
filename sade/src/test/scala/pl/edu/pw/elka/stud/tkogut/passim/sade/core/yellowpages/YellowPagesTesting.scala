package pl.edu.pw.elka.stud.tkogut.passim.sade.core.yellowpages
import org.scalatest._
import pl.edu.pw.elka.stud.tkogut.passim.sade.core._
import pl.edu.pw.elka.stud.tkogut.passim.sade.core.yellowpages._
import pl.edu.pw.elka.stud.tkogut.passim.sade.messages._
import scala.actors.Actor

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
    val agent = new Agent("John") with YelloPagesSearchable {
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
  }
  
  
  
  

}
