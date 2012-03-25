package pl.edu.pw.elka.stud.tkogut.sade.messages

import pl.edu.pw.elka.stud.tkogut.sade.core.Agent

case class DialogMessage(sender: Agent, dialogID: String) extends Message 