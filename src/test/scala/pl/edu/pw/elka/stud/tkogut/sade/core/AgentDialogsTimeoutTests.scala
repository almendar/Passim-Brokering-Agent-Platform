package pl.edu.pw.elka.stud.tkogut.sade.core

import org.scalatest.FunSuite
import pl.edu.pw.elka.stud.tkogut.sade.messages.Message
import java.util.concurrent.{TimeUnit, CountDownLatch}

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 06.05.12
 * Time: 19:17
 * To change this template use File | Settings | File Templates.
 */

class AgentDialogsTimeoutTests extends FunSuite {

  test("DialogInitiatorTimeOut") {

    val latch = new CountDownLatch(1)


    val res : Agent = new Agent("Responder") {
      protected def handleMessage(msg: Message) {}
      protected def processDialog(id: String) {}

      override def endDialog(id : String) {
        super.endDialog(id)
        if(!activeDialogs.isEmpty) {
          latch.countDown()
          assert(activeDialogs.isEmpty)
        }
        else {
          latch.countDown()
          killAgent()
        }
      }
      start()
    }
    val init = new Agent("Initiator") {
      protected def handleMessage(msg: Message) {
      }
      protected def processDialog(id: String) {
        killAgent()
      }
      start()
      this.establishDialog(res,null)
    }
    //Main thread wait
    latch.await()
    }


  test("Dialog keep-alive") {

    val latch = new CountDownLatch(2)


    val res : Agent = new Agent("Responder1") {
      protected def handleMessage(msg: Message) {}
      protected def processDialog(id: String) {}

      override def KeepAliveRequest(id:String) {
        latch.countDown()
        super.KeepAliveRequest(id)
      }
      start()
    }

    val init = new Agent("Initiator1") {
      protected def handleMessage(msg: Message) {
      }
      protected def processDialog(id: String) {
      }
      start()
      this.establishDialog(res,null)
    }


    //Two keep alive messages must get throught if the test be passed
    val b = latch.await(5*Dialog.DIALOG_TIMEOUT,TimeUnit.MILLISECONDS)
    res.killAgent()
    init.killAgent()
    assert(b)

  }
}
