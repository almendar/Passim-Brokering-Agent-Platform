package pl.edu.pw.elka.stud.tkogut.sade.core.yellowpages

import scala.collection.mutable.ListBuffer
import pl.edu.pw.elka.stud.tkogut.sade.core.Agent
import pl.edu.pw.elka.stud.tkogut.sade.messages._

final object YellowPagesAgent extends Agent("YelloPages") {

  protected[yellowpages] val book: ListBuffer[Agent] = new ListBuffer;

  def getNamesOfRegisteredAgents() = {
    val lb = new ListBuffer[String]
    for (ag <- book) {
      lb.append(ag.name)
    }
    book.map(_.name)
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
        case msg: SendAgentsMeetingConstraint => {
          val listToSend = sendSearchingAgentList(msg)
          speak("Sending request with sources")
          msg.from ! listToSend
        }
      })
  }

  private def sendSearchingAgentList(msg: SendAgentsMeetingConstraint) = {
    val filteredList = book.filter(msg.apply(_))
    AgentListQueryMessage(filteredList.toList)
  }

}



