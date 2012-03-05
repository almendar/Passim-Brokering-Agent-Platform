package pl.edu.pw.elka.stud.tkogut.passim.sade.messages

import pl.edu.pw.elka.stud.tkogut.passim.sade.messages.Message
import pl.edu.pw.elka.stud.tkogut.passim.sade.core.Agent

case class AgentList(list: List[Agent]) extends Message