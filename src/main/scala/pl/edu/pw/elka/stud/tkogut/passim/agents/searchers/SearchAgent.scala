package pl.edu.pw.elka.stud.tkogut.passim.agents.searchers
import pl.edu.pw.elka.stud.tkogut.passim.agents.Agent
import pl.edu.pw.elka.stud.tkogut.passim.agents.YelloPagesSearchable
import pl.edu.pw.elka.stud.tkogut.passim.messages.QueryMessage
import pl.edu.pw.elka.stud.tkogut.passim.messages.Message

abstract class SearchAgent(name: String) extends Agent(name) with YelloPagesSearchable {

  override def handleMessage(msg: Message) = {
    msg match {
      case query: QueryMessage => search(query)
      case _               => ;
    }
  }
  protected def search(query: QueryMessage);
}