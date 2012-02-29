package pl.edu.pw.elka.stud.tkogut.passim.agents

class Dialog(contact: Agent) {
  var mConfirmed: Boolean = false;
  val mContact: Agent = contact
  var nextAction: () => Unit = new Function0[Unit]{
        def apply() : Unit = {}
        }
}