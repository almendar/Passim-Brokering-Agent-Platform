package pl.edu.pw.elka.stud.tkogut.sade.core

import scala.actors._
import scala.collection.mutable.Map
import pl.edu.pw.elka.stud.tkogut.sade.messages.{DialogTimeoutMessage, KeeAliveMessage}


/**
 *
 * @param owner Agent that created this object dialog
 * @param contact Agent initiating this dialog.
 * @param id Id of the dialog.
 * @param isInitiated Tells if agent who poses this object initiated the dialog.
 */
final case class Dialog(owner:Agent, contact: Agent, id: String, isInitiated: Boolean = false) extends Actor {
  private var _updateTime = System.currentTimeMillis();
  private val ALIVE_MESSAGE = "Keep-Alive"
  private val OUTDATED_MESSAGE = "Check_Outdated"
  private val UPDATE_TIME = "Update_Time"
  private val SHUTDOWN_DIALOG = "SHUTDOWN_DIALOG"

  private var isBeingShutdown = false

  val isInitiatedByThis = isInitiated
  var isConfirmed: Boolean = false;
  var nextAction: String => Unit = new Function1[String, Unit] {
    def apply(dialogId: String): Unit = {}
  }
  var attributes = Map[String, Object]()

  start()

  def updateTime() {
    this ! UPDATE_TIME
  }

  def end() {
    this ! SHUTDOWN_DIALOG
  }

  def act() {

    if(isInitiated) sendKeepAlive else checkOutdated

    loop {
      receive {
        case ALIVE_MESSAGE => sendKeepAlive
        case OUTDATED_MESSAGE => checkOutdated
        case UPDATE_TIME =>  _updateTime = System.currentTimeMillis()
        case SHUTDOWN_DIALOG => exit()
      }
    }
  }

  private def sendKeepAlive  {
    val me = this
    contact ! KeeAliveMessage(dialogId = id)

   val a =  Actor.actor {
      Thread.sleep(Dialog.KEEP_ALIVE_TIME)
      me ! ALIVE_MESSAGE
    }
  }

  private def checkOutdated {
    val me = this
    val currentTime = System.currentTimeMillis()
    val isTimedOut = ((currentTime - _updateTime) > Dialog.DIALOG_TIMEOUT)
    println("Czy wiadomość była outdated:" + (currentTime - _updateTime) + " " + isTimedOut)
    if(isTimedOut) {
      owner ! DialogTimeoutMessage(id)
    }
    else {
      Actor.actor {
        Thread.sleep(Dialog.DIALOG_TIMEOUT)
        me ! OUTDATED_MESSAGE
      }
    }
  }

}

object Dialog {
  val DIALOG_TIMEOUT = 3000
  val KEEP_ALIVE_TIME = 1000
}
