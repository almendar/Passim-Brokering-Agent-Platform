package pl.edu.pw.elka.stud.tkogut.passim.brokering
import pl.edu.pw.elka.stud.tkogut.passim.sade.core.yellowpages.YelloPagesSearchable
import pl.edu.pw.elka.stud.tkogut.passim.sade.core.Agent
import pl.edu.pw.elka.stud.tkogut.passim.sade.messages.Message
import pl.edu.pw.elka.stud.tkogut.passim.brokering.messages.QueryMessage


abstract class SearchAgent(name: String) extends Agent(name) with YelloPagesSearchable {
 
  override def handleMessage(msg: Message) = {
    msg match {
      case query: QueryMessage => search(query)
      case _               => ;
    }
  }
  protected def search(query: QueryMessage);
}