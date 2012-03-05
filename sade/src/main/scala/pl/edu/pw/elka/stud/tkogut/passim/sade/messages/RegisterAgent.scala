package pl.edu.pw.elka.stud.tkogut.passim.sade.messages

import pl.edu.pw.elka.stud.tkogut.passim.sade.core.Agent
import pl.edu.pw.elka.stud.tkogut.passim.sade.core.yellowpages.YelloPagesSearchable

case class RegisterAgent(from: YelloPagesSearchable, description: String) extends Message