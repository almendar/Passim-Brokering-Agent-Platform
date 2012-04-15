package pl.edu.pw.elka.stud.tkogut.sade.core

import scala.collection.mutable.Map

final case class Dialog(contact: Agent, id: String, isInitiated: Boolean = false) {
  val isInitiatedByThis = isInitiated
  var isConfirmed: Boolean = false;
  var nextAction: String => Unit = new Function1[String, Unit] {
    def apply(dialogId: String): Unit = {}
  }
  var attributes = Map[String, Object]()
}