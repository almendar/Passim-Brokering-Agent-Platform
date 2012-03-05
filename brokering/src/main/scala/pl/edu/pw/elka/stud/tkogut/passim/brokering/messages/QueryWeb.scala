package pl.edu.pw.elka.stud.tkogut.passim.brokering.messages
import pl.edu.pw.elka.stud.tkogut.passim.sade.messages.Message

case class QueryWeb(q: String, dialogId: String) extends Message