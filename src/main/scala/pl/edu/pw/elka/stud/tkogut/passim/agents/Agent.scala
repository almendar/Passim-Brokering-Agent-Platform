package pl.edu.pw.elka.stud.tkogut.passim.agents
import scala.actors.Actor
import scala.collection.mutable.HashMap
import pl.edu.pw.elka.stud.tkogut.passim.messages.EstablishDialogMessage
import java.util.UUID
import pl.edu.pw.elka.stud.tkogut.passim.messages.Message
import com.weiglewilczek.slf4s._

/**
 * Base class for all Agents in the system
 *
 * @author Tomasz Kogut
 */
abstract class Agent(agentName: String) extends Actor with Logging {

  protected val activeDialogs = new HashMap[String, Dialog]

  protected def handleMessage(msg: Message)

  protected def processDialog(id: String)

  /**
   * Return Agent's name
   */
  final def name: String = agentName

  /**
   * Method make agent to speak text with his name.
   *
   * @param text
   */
  final def speak(text: String) {
    logger.debug(text)
  }

  /**
   * Agents processing message loop.
   * Automatically can establish a dialog with someone who demands it.
   * Other messages are passed to @see{handleMessage}
   */
  def act = {
    loop {
      receive {
        case dialogEstablishRequest: EstablishDialogMessage =>
          speak("Request to establish dialog from:" + dialogEstablishRequest.from.name)
          activeDialogs += (dialogEstablishRequest.dialogID -> new Dialog(dialogEstablishRequest.from));
          confirmDialog(dialogEstablishRequest.from, dialogEstablishRequest.dialogID)
        case ("OK", id: String) =>
          activeDialogs(id).mConfirmed = true
          speak("Dialog confirmed:" + id)
          processDialog(id)
        case x: Message => handleMessage(x)
        case _ => null
      }
    }
  }

  final protected def establishDialog(adress: Agent): String = {
    val dialogId = generateID()
    speak("Sending request for dialog with id:" + dialogId + " to:" + adress.name)
    adress ! new EstablishDialogMessage(this, dialogId)
    val t = new Dialog(adress)
    activeDialogs += (dialogId -> t)
    return dialogId
  }

  private def confirmDialog(adress: Agent, dialogId: String) = {
    speak("Confirming dialog:" + dialogId + " with:" + adress.name)
    adress ! ("OK", dialogId)
  }

  protected def generateID(): String = {
    UUID.randomUUID().toString()
  }
}