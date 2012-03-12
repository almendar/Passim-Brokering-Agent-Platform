package pl.edu.pw.elka.stud.tkogut.passim.sade.core;

import org.scalatest._
import pl.edu.pw.elka.stud.tkogut.passim.sade.messages._

case class StartDialog() extends Message

class AgentsCoreFunctionsTest extends FunSuite {

  val initiatorAgent = new Agent("Initiator") {

    override def handleMessage(msg: Message) {
      speak("1")
      msg match {
        case x1: StartDialog => println("!"); establishDialog(respondingAgent, () => this.exit())
        case _ => println("??")
      }
    }

    override def processDialog(id: String) {
      val dialog = activeDialogs(id)
      assert(dialog.contact == (respondingAgent))
      assert(dialog.mConfirmed == true)
      dialog.nextAction()
    }

  }

  val respondingAgent = new Agent("Responder") {
    override def processDialog(id: String) = {}
    override def handleMessage(msg: Message) = {}
  }

  initiatorAgent.start
  respondingAgent.start

  test("Dialog establish dialog") {
    initiatorAgent !  StartDialog()
    Thread.sleep(1000)
  }
}
