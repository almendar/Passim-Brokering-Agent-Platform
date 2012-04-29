package pl.edu.pw.elka.stud.tkogut.sade.core

import scala.collection.mutable.Map

/**
 *
 * @param contact Agent initiating this dialog.
 * @param id Id of the dialog.
 * @param isInitiated Tells if agent who poses this object initiated the dialog.
 */
final case class Dialog(contact: Agent, id: String, isInitiated: Boolean = false) {
  val creationTime = System.currentTimeMillis();
  val isInitiatedByThis = isInitiated
  var isConfirmed: Boolean = false;
  var nextAction: String => Unit = new Function1[String, Unit] {
    def apply(dialogId: String): Unit = {}
  }
  var attributes = Map[String, Object]()

}


object Dialog {
  val DIALOG_TIMEOUT = 3000
}