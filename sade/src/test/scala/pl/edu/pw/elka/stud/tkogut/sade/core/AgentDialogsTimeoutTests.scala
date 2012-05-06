package pl.edu.pw.elka.stud.tkogut.sade.core

import org.scalatest.FunSuite
import pl.edu.pw.elka.stud.tkogut.sade.messages.Message
import actors.Actor

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 06.05.12
 * Time: 19:17
 * To change this template use File | Settings | File Templates.
 */

class AgentDialogsTimeoutTests extends FunSuite {
  test("DialogInitiatorTimeOut") {
    //Those test are wrong !!! TODO
    // Use count-down latch
    val o = new Object

    val res = new Agent("Responder") {
      protected def handleMessage(msg: Message) {}
      protected def processDialog(id: String) {}
      start()
    }


    val init = new Agent("Initiator") {

      establishDialog(res,null)
      protected def handleMessage(msg: Message) {

      }

      protected def processDialog(id: String) {
        speak("Umieram")
        exit()
      }
      start()
    }

    Actor.actor {
      Thread.sleep(15000);
      println("Budzę")
      o.synchronized(o.notifyAll())
    }

    o.synchronized {
      o.wait
    }
     assert(res.activeDialogs.isEmpty)
     println("Wychodzę")

  }
}
