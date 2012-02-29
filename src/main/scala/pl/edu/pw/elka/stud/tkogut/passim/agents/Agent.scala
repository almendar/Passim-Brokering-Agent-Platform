package pl.edu.pw.elka.stud.tkogut.passim.agents
import scala.actors.Actor
import scala.collection.mutable.HashMap
import pl.edu.pw.elka.stud.tkogut.passim.messages.EstablishDialog
import java.util.UUID
import pl.edu.pw.elka.stud.tkogut.passim.messages.Message

abstract class Agent(agentName: String) extends Actor {

  val name: String = agentName

  def speak(text: String) {
    //println(name + ": " + text)
  }

  val activeDialogs = new HashMap[String, Dialog]

  def act = {
    loop {
      receive {
        case dialogDemand: EstablishDialog =>
          speak("Request to establish dialog from:" + dialogDemand.from.name)
          activeDialogs += (dialogDemand.dialogId -> new Dialog(dialogDemand.from));
          confirmDialog(dialogDemand.from, dialogDemand.dialogId)
        case ("OK", id: String) =>
          activeDialogs(id).mConfirmed = true
          speak("Dialog confirmed:" + id)
          processDialog(id)
        case x: Message => handleMessage(x)
        case _          => null
      }
    }
  }

  def handleMessage(msg: Message)

  def processDialog(id: String)

  def establishDialog(adress: Agent, 
      nextAction: () => Unit = new Function0[Unit]{
        def apply() : Unit = {}
        }
      ): String = {
    val dialogId = generateID()
    speak("Sending request for dialog with id:" + dialogId + " to:" + adress.name)
    adress ! EstablishDialog(this, dialogId)
    val t = new Dialog(adress)
    t.nextAction = nextAction
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