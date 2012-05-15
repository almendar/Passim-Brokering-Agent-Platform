package pl.edu.pw.elka.stud.tkogut.sade.core
import scala.actors.Actor
import scala.collection.mutable.HashMap
import pl.edu.pw.elka.stud.tkogut.sade.messages._
import java.util.UUID
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

  Logging.configure { log =>
    log.registerWithJMX = true

    log.level = Level.INFO
    log.loggers("com.myproject.weebits") = Level.OFF

    log.console.enabled = true
    log.console.threshold = Level.INFO
  }


  private val logger  = Log.forName(Agent.getClass.toString)

  protected[core] val activeDialogs = new HashMap[String, Dialog]

  protected def handleMessage(msg: Message)

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
      receive {
        case dialogEstablishRequest: EstablishDialogMessage =>
          speak("Request to establish dialog from:" + dialogEstablishRequest.from.name)
          val d = new Dialog(dialogEstablishRequest.from, dialogEstablishRequest.dialogID)
          activeDialogs += (dialogEstablishRequest.dialogID -> d);
          confirmDialog(dialogEstablishRequest.from, dialogEstablishRequest.dialogID)
          monitorDialogTimeOut(d)
        case (Agent.OK, id: String) =>
          activeDialogs(id).isConfirmed = true
          speak("Dialog confirmed:" + id)
          KeepAliveRequest(id)
          processDialog(id)
        case (Agent.BYE, id: String) =>
          if (activeDialogs.contains(id)) activeDialogs -= id
        case DialogTimeoutMessage(id) =>
          val dialog = activeDialogs.getOrElse(id, null);
          monitorDialogTimeOut(dialog)
        case KeepAliveMessage(id) =>
          KeepAliveRequest(id)
        case Agent.DIE =>
          speak("I am dying...")
          this.exit()
        case x: Message => handleMessage(x)
        case y: Any => speak("Unknown message received:" + y)
      }
    }
  }


  protected def KeepAliveRequest(id: String) {
    speak("Got keep-alive message for dialog id:" + id)
    val dialog = activeDialogs.getOrElse(id, null);
    if (dialog != null) {
      if (dialog.isInitiated)
        sendKeepAlive(dialog)
      else {
        speak("Updating dialog: %s time".format(id))
        dialog.updateTime()
      }
    }
    else {
      speak("Dialog already closed:%s"
        .format(id))
    }
  }

  final protected def establishDialog(adress: Agent, nextAction: String => Unit = new Function1[String, Unit] {
      def apply(id: String): Unit = {}
    }): String =
  {
    val dialogId = generateID()
    speak("Sending request for dialog with id:" + dialogId + " to:" + adress.name)
    adress ! new EstablishDialogMessage(this, dialogId)
    val t = new Dialog(adress, dialogId,isInitiated = true)
    t.nextAction = nextAction
    activeDialogs += (dialogId -> t)
    return dialogId
  }


  protected def endDialog(id: String) {
    speak("Ending dialog:"+id)
    val d = activeDialogs(id)
    activeDialogs -= (id)
    d.contact ! (Agent.BYE, id)
  }

  final private def confirmDialog(adress: Agent, dialogId: String) {
    speak("Confirming dialog:" + dialogId + " with:" + adress.name)
    adress ! (Agent.OK, dialogId)
  }

  final protected def generateID(): String = {
    UUID.randomUUID().toString()
  }

  /**
   * This method can be safely called from anywhere cause it is asynchronous.
   * It will send a message to this agent telling to kill himself.
   */
  final def killAgent()  {
      this ! Agent.DIE
  }

  private def sendKeepAlive(dialog:Dialog)  {
    dialog.contact ! KeepAliveMessage(dialogId = dialog.id)
    val me = this
    Actor.actor {
        Thread.sleep(Dialog.KEEP_ALIVE_TIME)
        me ! KeepAliveMessage(dialog.id)
      }
  }

  private def monitorDialogTimeOut(dialog:Dialog) {
    speak("Monitoring timeout of dialog: %s".format(dialog.id))
    val currentTime = System.currentTimeMillis()
    val dt = (currentTime - dialog._updateTime)
    speak("Dialog dt=%d".format(dt))
    val isTimedOut = (dt > Dialog.DIALOG_TIMEOUT)
    if(isTimedOut) {
      speak("Dialog %s is timedout".format(dialog.id))
      endDialog(dialog.id)
    }
    else {
      val me = this
      Actor.actor {
        Thread.sleep(Dialog.DIALOG_TIMEOUT)
        me ! DialogTimeoutMessage(dialog.id)
      }
  }

  }
}
