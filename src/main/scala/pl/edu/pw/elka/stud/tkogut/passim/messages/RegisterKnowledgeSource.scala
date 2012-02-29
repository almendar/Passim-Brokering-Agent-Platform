package pl.edu.pw.elka.stud.tkogut.passim.messages

import pl.edu.pw.elka.stud.tkogut.passim.agents.Agent
import pl.edu.pw.elka.stud.tkogut.passim.agents.YelloPagesSearchable

case class RegisterKnowledgeSource(from: YelloPagesSearchable, description: String) extends Message