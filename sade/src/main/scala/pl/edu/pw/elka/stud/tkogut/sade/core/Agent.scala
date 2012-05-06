package pl.edu.pw.elka.stud.tkogut.sade.core
import scala.actors.Actor
import scala.collection.mutable.HashMap
import pl.edu.pw.elka.stud.tkogut.sade.core._
import pl.edu.pw.elka.stud.tkogut.sade.messages._
import java.util.UUID

object Agent {
  final val OK = "OK"
  final val BYE = "BYE"
}

/**
 * Base class for all Agents in the system
 *
 * @author Tomasz Kogut
 */
abstract class Agent(agentName: String) extends Actor {

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
   println(name + ":" + Obj.toString)
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
          activeDialogs += (dialogEstablishRequest.dialogID -> new Dialog(this,dialogEstablishRequest.from, dialogEstablishRequest.dialogID));
          confirmDialog(dialogEstablishRequest.from, dialogEstablishRequest.dialogID)
        case (Agent.OK, id: String) =>
          activeDialogs(id).isConfirmed = true
          speak("Dialog confirmed:" + id)
          processDialog(id)
        case (Agent.BYE, id: String) =>
          if (activeDialogs.contains(id)) activeDialogs -= id
        case DialogTimeoutMessage(id) =>
          endDialog(id)
        case KeeAliveMessage(id) =>
          //TODO this can be not true
          speak("Got keep-alive message for dialog id:" + id)
          activeDialogs(id).updateTime()
        case x: Message => handleMessage(x)
        case y: Any => speak("Unknown message received:" + y)
      }
    }
  }

  final protected def establishDialog(adress: Agent,
    nextAction: String => Unit = new Function1[String, Unit] {
      def apply(id: String): Unit = {}
    }): String = {
    val dialogId = generateID()
    speak("Sending request for dialog with id:" + dialogId + " to:" + adress.name)
    adress ! new EstablishDialogMessage(this, dialogId)
    val t = new Dialog(this,adress, dialogId)
    t.nextAction = nextAction
    activeDialogs += (dialogId -> t)
    return dialogId
  }

  final protected def endDialog(id: String) {
    speak("Ending dialog:"+id)
    val d = activeDialogs(id)
    activeDialogs -= (id)
    d.contact ! (Agent.BYE, id)
    d.end()
  }

  final private def confirmDialog(adress: Agent, dialogId: String) = {
    speak("Confirming dialog:" + dialogId + " with:" + adress.name)
    adress ! (Agent.OK, dialogId)
  }

  final protected def generateID(): String = {
    UUID.randomUUID().toString()
  }

  final def killAgent()  {
       this.exit()
  }

}
