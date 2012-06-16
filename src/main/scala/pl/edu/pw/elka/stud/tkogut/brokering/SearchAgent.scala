package pl.edu.pw.elka.stud.tkogut.brokering

import pl.edu.pw.elka.stud.tkogut.sade.core._
import pl.edu.pw.elka.stud.tkogut.sade.core.yellowpages._
import pl.edu.pw.elka.stud.tkogut.sade.messages._
import pl.edu.pw.elka.stud.tkogut.brokering.messages._

abstract class SearchAgent(name: String) extends Agent(name) with YellowPagesSearchable {

  override def handleMessage(msg: Message) =
  {
    msg match
    {
      case query: QueryMessage => search(query)
      //case _ => ;
    }
  }
  protected def search(query: QueryMessage);
  final override def processDialog(id: String) {}
}