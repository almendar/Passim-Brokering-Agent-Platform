package pl.edu.pw.elka.stud.tkogut.sade.messages

import pl.edu.pw.elka.stud.tkogut.sade.core._

/**
 *
 * @param from Inititiator agent of this Dialog
 * @param dialogID Id of the dialog
 */
case class EstablishDialogMessage(from: Agent, dialogID: String) extends Message