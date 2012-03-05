package pl.edu.pw.elka.stud.tkogut.passim.sade.messages

import pl.edu.pw.elka.stud.tkogut.passim.sade.core._
import pl.edu.pw.elka.stud.tkogut.passim.sade.messages._

case class EstablishDialogMessage(from: Agent, dialogID: String) extends Message