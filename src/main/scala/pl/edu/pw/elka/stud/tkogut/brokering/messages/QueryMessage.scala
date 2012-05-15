package pl.edu.pw.elka.stud.tkogut.brokering.messages

import pl.edu.pw.elka.stud.tkogut.sade.messages._

case class QueryMessage(query: String, dialogId: String) extends Message