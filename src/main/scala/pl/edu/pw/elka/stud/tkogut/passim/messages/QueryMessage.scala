package pl.edu.pw.elka.stud.tkogut.passim.messages
import pl.edu.pw.elka.stud.tkogut.passim.agents.Agent

case class QueryMessage(query: String, dialogId: String) extends Message