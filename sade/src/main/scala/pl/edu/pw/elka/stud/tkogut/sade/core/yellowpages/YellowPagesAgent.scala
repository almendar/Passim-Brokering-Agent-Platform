package pl.edu.pw.elka.stud.tkogut.sade.core.yellowpages

import scala.collection.mutable.ListBuffer
import scala.actors.Actor
import pl.edu.pw.elka.stud.tkogut.sade.core.Agent
import pl.edu.pw.elka.stud.tkogut.sade.messages._

final object YellowPagesAgent extends Agent("YelloPages") {

  protected[yellowpages] val book: ListBuffer[Agent] = new ListBuffer;

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
        case msg: SendAgentsMeetingConstraint => sendSearchingAgentList(msg)
      })
  }

  private def sendSearchingAgentList(msg: SendAgentsMeetingConstraint): Unit = {
    val filteredList = book.filter(msg.apply(_))
    val listToSend = new AgentList(filteredList.toList)
    speak("Sending request with sources")
    sender ! listToSend
  }

}



