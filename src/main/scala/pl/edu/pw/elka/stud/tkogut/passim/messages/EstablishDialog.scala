package pl.edu.pw.elka.stud.tkogut.passim.messages

import pl.edu.pw.elka.stud.tkogut.passim.agents.Agent

case class EstablishDialog(from: Agent, dialogId: String) extends Message