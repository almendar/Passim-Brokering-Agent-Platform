package pl.edu.pw.elka.stud.tkogut.passim.sade.core.yellowpages

import scala.collection.mutable.ListBuffer
import scala.actors.Actor
import pl.edu.pw.elka.stud.tkogut.passim.sade.core.Agent
import pl.edu.pw.elka.stud.tkogut.passim.sade.messages._

final object YellowPagesAgent extends Agent("YelloPages") {

  protected val book: ListBuffer[Agent] = new ListBuffer;

  def getNamesOfRegisteredAgents() = {
    val lb = new ListBuffer[String]
    for (ag <- book) {
      lb.append(ag.name)
    }
    lb.toList
  }

  override def handleMessage(msg: Message) = {}

  override def processDialog(id: String) = {}

  override def act = loop {
    (
      receive {
        case msg: RegisterAgent =>
          msg.from match {
            case x: Agent => book.append(x)
            case _ =>
          }

        //  speak("Registered knowledge source:" + msg.description)
        case msg: SendWebSearchSourceRequest => sendSearchingAgentList(msg)
      })
  }

  private def sendSearchingAgentList(msg: SendWebSearchSourceRequest): Unit = {
    val searchAgents = ListBuffer[Agent]()
    book.foreach(_ match {
      case x: Agent => searchAgents.append(x)
      case _ => false
    })
    val listToSend = new AgentList(searchAgents.toList)
    speak("Sending request with sources")
    sender ! listToSend
  }

}



