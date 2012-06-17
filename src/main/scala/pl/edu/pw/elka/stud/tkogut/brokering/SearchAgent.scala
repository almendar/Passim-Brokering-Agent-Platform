package pl.edu.pw.elka.stud.tkogut.brokering

import dialect.Attribute
import pl.edu.pw.elka.stud.tkogut.sade.core._
import pl.edu.pw.elka.stud.tkogut.sade.core.yellowpages._
import pl.edu.pw.elka.stud.tkogut.sade.messages._
import pl.edu.pw.elka.stud.tkogut.brokering.messages._

abstract class SearchAgent(name: String) extends Agent(name) with YellowPagesSearchable {

  val capabilities : List[Attribute]

  override def handleMessage(msg: Message) =
  {
    msg match
    {
      case query: QueryMessage => search(query)
      //case _ => ;
    }
  }


  /**
   * This does nothing since search agent never should establish dialogs on their own.
   */
  final override def processDialog(id: String) {}

  protected def search(query: QueryMessage);

}