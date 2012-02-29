package pl.edu.pw.elka.stud.tkogut.passim.messages

import pl.edu.pw.elka.stud.tkogut.passim.agents.Agent

case class EstablishDialogMessage(from: Agent, dialogID: String) extends Message