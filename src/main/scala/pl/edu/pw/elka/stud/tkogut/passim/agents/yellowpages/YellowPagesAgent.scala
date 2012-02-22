package pl.edu.pw.elka.stud.tkogut.passim.agents.yellowpages

import scala.collection.mutable.ListBuffer
import scala.actors.Actor
import pl.edu.pw.elka.stud.tkogut.passim.messages._
import pl.edu.pw.elka.stud.tkogut.passim.agents.Agent
import pl.edu.pw.elka.stud.tkogut.passim.agents.YelloPagesSearchable
import pl.edu.pw.elka.stud.tkogut.passim.agents.Agent
import pl.edu.pw.elka.stud.tkogut.passim.agents.searchers.SearchAgent
import pl.edu.pw.elka.stud.tkogut.passim.agents.searchers.SearchAgent

object YellowPagesAgent extends Agent("YelloPages") {

  val book: ListBuffer[Agent] = new ListBuffer;

  def getNameOfRegisteredAgents() = {
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
        case msg: RegisterKnowledgeSource =>
          msg.srcObject match {
            case x: Agent => book.append(x)
            case _        =>
          }

        //  speak("Registered knowledge source:" + msg.description)
        case msg: SendWebSearchSourceRequest => sendSearchingAgentList(msg)
      })
  }

  private def sendSearchingAgentList(msg: SendWebSearchSourceRequest): Unit = {
    val searchAgents = ListBuffer[SearchAgent]()
    book.foreach(_ match {
      case x: SearchAgent => searchAgents.append(x)
      case _              => false
    })
    val listToSend = new WebSearchSourceList(searchAgents.toList)
    speak("Sending request with sources")
    msg.from ! listToSend
  }

}



