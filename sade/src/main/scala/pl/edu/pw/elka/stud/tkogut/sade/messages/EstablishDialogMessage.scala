package pl.edu.pw.elka.stud.tkogut.sade.messages

import pl.edu.pw.elka.stud.tkogut.sade.core._
import pl.edu.pw.elka.stud.tkogut.sade.messages._

case class EstablishDialogMessage(from: Agent, dialogID: String) extends Message