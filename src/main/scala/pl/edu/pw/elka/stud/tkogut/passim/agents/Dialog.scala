package pl.edu.pw.elka.stud.tkogut.passim.agents

final case class Dialog(contact: Agent) {
  var mConfirmed: Boolean = false;
  var nextAction: () => Unit = new Function0[Unit]{
        def apply() : Unit = {}
        }
}