package pl.edu.pw.elka.stud.tkogut.passim.brokering.messages

import pl.edu.pw.elka.stud.tkogut.passim.sade.messages.Message

case class QueryMessage(query: String, dialogId: String) extends Message