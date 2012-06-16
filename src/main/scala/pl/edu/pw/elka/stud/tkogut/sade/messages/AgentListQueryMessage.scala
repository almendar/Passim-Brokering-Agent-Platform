package pl.edu.pw.elka.stud.tkogut.sade.messages
import pl.edu.pw.elka.stud.tkogut.sade.core.Agent


case class AgentListQueryMessage(list: List[Agent]) extends Message