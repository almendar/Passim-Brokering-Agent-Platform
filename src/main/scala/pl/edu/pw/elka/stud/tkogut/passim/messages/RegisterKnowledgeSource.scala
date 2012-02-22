package pl.edu.pw.elka.stud.tkogut.passim.messages

import pl.edu.pw.elka.stud.tkogut.passim.agents.Agent
import pl.edu.pw.elka.stud.tkogut.passim.agents.YelloPagesSearchable

case class RegisterKnowledgeSource(srcObject: YelloPagesSearchable, description: String) extends Message