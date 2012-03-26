package pl.edu.pw.elka.stud.tkogut.sade.core;

import org.scalatest._
import pl.edu.pw.elka.stud.tkogut.sade.messages._
import scala.actors._

case class StartDialog(withWho: Agent) extends Message
case class CheckDialogId(dialogId: String) extends Message
case class IdIsOK() extends Message
case class CheckIfDialogIsRemoved(id: String) extends Message

class AgentsCoreFunctionsTest extends FunSuite {

  var initDialog: String = null
  var responderDialog: String = null

  test("Dialog establish and end") {

    val respondingAgent = new Agent("Responder") {
      override def processDialog(id: String) = {}
      override def handleMessage(msg: Message) = {
        msg match {
          case CheckDialogId(id: String) =>
            assert(activeDialogs(id).id == initDialog)
            sender ! IdIsOK()
          case CheckIfDialogIsRemoved(id: String) =>
            assert(!activeDialogs.contains(id))
        }
      }
    }
    val initiatorAgent = new Agent("Initiator") {
      var lastId: String = null
      override def handleMessage(msg: Message) {
        msg match {
          case StartDialog(a: Agent) =>
            initDialog = establishDialog(a,
              (id: String) => {
                assert(initDialog != null)
                assert(activeDialogs(id).isConfirmed)
                respondingAgent ! CheckDialogId(id)
              })
          case IdIsOK() =>
            endDialog(initDialog)
            respondingAgent ! CheckIfDialogIsRemoved(initDialog)

        }
      }
      override def processDialog(id: String) {
        val dialog = activeDialogs(id)
        dialog.nextAction(id)
      }
    }

    //Start agents
    initiatorAgent.start
    respondingAgent.start

    //Start the conversation
    initiatorAgent ! StartDialog(respondingAgent)

    //assert(initDialog == responderDialog)

  }
}
