package pl.edu.pw.elka.stud.tkogut.sade.core

import scala.actors.Actor
import scala.collection.mutable.HashMap
import pl.edu.pw.elka.stud.tkogut.sade.messages._
import com.codahale.logula._
import org.apache.log4j.Level

object Agent {
  final val OK = "OK"
  final val BYE = "BYE"
  final val DIE = "DIE"
}

/**
 * Base class for all Agents in the system
 *
 * @author Tomasz Kogut
 */
abstract class Agent(agentName: String) extends Actor {

  Logging.configure {
    log =>
      log.registerWithJMX = true
      log.level = Level.INFO
      //log.loggers("com.myproject.weebits") = Level.OFF
      log.console.enabled = true
      log.console.threshold = Level.INFO
  }


  val logger = Log.forName(Agent.getClass.toString)

  //@TODO Think about this.
  //
  var dialogMgr: DialogManager = new DialogManager(this)


  /**
   * This method is called when message is received.
   * Should be overwritten to handle different types of messages.
   * @param msg
   */
  protected def handleMessage(msg: Message)

  /**
   * This method is called when dialog is confirmed.
   * @param id
   */
  protected def processDialog(id: String)

  /**
   * Return Agent's name
   */
  final def name: String = agentName

  /**
   * Method make agent to speak text with his name.
   *
   * @param Obj
   */
  final def speak(Obj: Any) {
    //   println(name + ":" + Obj.toString)
    this.logger.info(Obj.toString)
  }

  /**
   * Agents processing message loop.
   * Automatically can establish a dialog with someone who demands it.
   * Other messages are passed to @see{handleMessage}
   */
  def act = {
    speak("Started to act.")
    loop {
      receiveWithin(500) {
        case dialogEstablishRequest: EstablishDialogMessage =>
          speak("Request to establish dialog from:" + dialogEstablishRequest.from.name)
          dialogMgr.establishResponseDialog(dialogEstablishRequest.from, dialogEstablishRequest.dialogID)
          dialogMgr.confirmDialog(dialogEstablishRequest.dialogID)
          speak("Confirming dialog:" + dialogEstablishRequest.dialogID + " with:" + dialogEstablishRequest.from)
        //monitorDialogTimeOut(d)
        case (Agent.OK, id: String) =>
          dialogMgr.setDialogConfirmed(id)
          speak("Dialog confirmed:" + id)
          //KeepAliveRequest(id)
          processDialog(id)
        case (Agent.BYE, id: String) =>
          speak("Dialog ended by:"+ dialogMgr.getContact(id))
          dialogMgr.removeDialog(id)
        case KeepAliveMessage(id) =>
          KeepAliveRequest(id)
        case Agent.DIE =>
          speak("I am dying...")
          this.exit()
        case x: Message => handleMessage(x)
        case y: Any => speak("Unknown message received:" + y)
      }
      endOutOfTimeDialogs()
      refreshMyDialogs()
    }
  }

  /**
   * Refreshes and notifies agents that dialogs should still be valid.
   */
  def refreshMyDialogs() {
    val toBeUpdated: Iterable[String] = dialogMgr.getDialogsThatShouldBeUpdated()
    toBeUpdated.foreach {
      k: String =>
        dialogMgr.updateTime(k)
        dialogMgr.getContact(k) ! KeepAliveMessage(dialogId = k)
    }
  }

  /**
   * Discards all dialogs that are out-of-time.
   */
  def endOutOfTimeDialogs() {
    val timedOutDialogs: Iterable[String] = dialogMgr.getOutdatedDialogs()
    timedOutDialogs.foreach {
      id: String =>
        dialogMgr.endDialogWithAgent(id)
    }
  }

  /**
   * Invoked when dialog should be refreshed not to get outdated.
   * @param id
   */
  protected def KeepAliveRequest(id: String) {
    speak("Got keep-alive message for dialog id:" + id)

    if (dialogMgr.hasDialog(id)) {
      dialogMgr.updateTime(id)
      speak("Updating dialog: %s time".format(id))
    }
    else {
      speak("Dialog already closed:%s".format(id))
      dialogMgr.endDialogWithAgent(id)
    }
  }


  /**
   * Method established dialog with another agent.
   * @param adress Reference to agent we wanto to contact.
   * @param nextAction Pointer to callback f-ction that can be invoked when dialog is established.
   * @return Id of established dialog.
   */
  final protected def establishDialog(adress: Agent, nextAction: String => Unit = new Function1[String, Unit] {
    def apply(id: String): Unit = {}
  }): String = {
    val dialogId = dialogMgr.establishDialog(adress)
    dialogMgr.setDialogNextAction(dialogId, nextAction)
    speak("Sending request for dialog with id:" + dialogId + " to:" + adress.name)
    adress ! new EstablishDialogMessage(this, dialogId)
    return dialogId
  }

  /*
  protected def endDialog(id: String) {
    speak("Ending dialog:"+id)
    val contact = dialogMgr.getContact(id)
    dialogMgr.removeDialog(id)
    contact ! (Agent.BYE, id)
  }
  */


  /**
   * This method can be safely called from anywhere cause it is asynchronous.
   * It will send a message to this agent telling to kill himself.
   */
  final def killAgent() {
    this ! Agent.DIE
  }

  /**
   * Will give name of the Agent
   * @return Agent's name.
   */
  override def toString() : String = {
    return name
  }
}
