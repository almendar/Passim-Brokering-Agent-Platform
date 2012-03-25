package pl.edu.pw.elka.stud.tkogut.sade.messages

import pl.edu.pw.elka.stud.tkogut.sade.core.Agent
import pl.edu.pw.elka.stud.tkogut.sade.core.yellowpages.YelloPagesSearchable

case class RegisterAgent(from: YelloPagesSearchable, description: String) extends Message