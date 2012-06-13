package pl.edu.pw.elka.stud.tkogut.sade.core;

import org.scalatest._
import pl.edu.pw.elka.stud.tkogut.sade.messages._
import java.util.concurrent.{TimeUnit, CountDownLatch}

case class StartDialog(withWho: Agent) extends Message
case class CheckDialogId(dialogId: String) extends Message
case class IdIsOK() extends Message
case class CheckIfDialogIsRemoved(id: String) extends Message

class AgentsCoreFunctionsTest extends FunSuite {

  var initDialog: String = null
  var responderDialog: String = null

  test("Dialog establish and end") {

    val latch = new CountDownLatch(2)

    val respondingAgent = new Agent("Responder") {
      override def processDialog(id: String)  {}
      override def handleMessage(msg: Message)  {
        msg match {
          case CheckDialogId(id: String) =>
            assert(dialogMgr.getContact(id) ==
              dialogMgr.getContact(initDialog))
            sender ! IdIsOK()
          case CheckIfDialogIsRemoved(id: String) =>
            assert(!dialogMgr.hasDialog(id))
           latch.countDown()
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
                assert(dialogMgr.isConfirmed(id))
                respondingAgent ! CheckDialogId(id)
              })
          case IdIsOK() =>
            dialogMgr.endDialogWithAgent(initDialog)
            respondingAgent ! CheckIfDialogIsRemoved(initDialog)
            latch.countDown()
        }
      }
      override def processDialog(id: String) {
        dialogMgr.invokeAction(id)
      }
    }

    //Start agents
    initiatorAgent.start
    respondingAgent.start

    //Start the conversation
    initiatorAgent ! StartDialog(respondingAgent)
    latch.await(5,TimeUnit.SECONDS)
    //assert(initDialog == responderDialog)

  }



}
