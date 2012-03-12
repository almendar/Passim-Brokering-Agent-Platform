package pl.edu.pw.elka.stud.tkogut.passim.sade.core

final case class Dialog(contact: Agent, id: String, isInitiated: Boolean = false) {
  val isInitiatedByThis = isInitiated
  var isConfirmed: Boolean = false;
  var nextAction: String => Unit = new Function1[String, Unit] {
    def apply(dialogId: String): Unit = {}
  }
}