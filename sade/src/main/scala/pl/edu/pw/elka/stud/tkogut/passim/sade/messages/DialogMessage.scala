package pl.edu.pw.elka.stud.tkogut.passim.sade.messages

import pl.edu.pw.elka.stud.tkogut.passim.sade.core.Agent

case class DialogMessage(sender: Agent, dialogID: String) extends Message 