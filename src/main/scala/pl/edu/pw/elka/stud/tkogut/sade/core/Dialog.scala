package pl.edu.pw.elka.stud.tkogut.sade.core

import scala.collection.mutable.Map


/**
 *
 * @param contact Agent initiating this dialog.
 * @param id Id of the dialog.
 * @param isInitiated Tells if agent who poses this object initiated the dialog.
 */
final case class Dialog(contact: Agent, id: String, isInitiated: Boolean = false) {
  var _updateTime = System.currentTimeMillis();
  var isTimedOut = false
  val isInitiatedByThis = isInitiated
  var isConfirmed: Boolean = false;
  var nextAction: String => Unit = new Function1[String, Unit] {
    def apply(dialogId: String): Unit = {}
  }
  var attributes = Map[String, Object]()
  def updateTime() = _updateTime = System.currentTimeMillis()
}

object Dialog {
  val DIALOG_TIMEOUT = 10000
  val KEEP_ALIVE_TIME = 500

}
