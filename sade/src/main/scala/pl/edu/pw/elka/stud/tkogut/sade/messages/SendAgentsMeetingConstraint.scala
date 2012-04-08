package pl.edu.pw.elka.stud.tkogut.sade.messages

import pl.edu.pw.elka.stud.tkogut.sade.core.Agent

case class SendAgentsMeetingConstraint(from:Agent, constraingFunc: Function1[Agent, Boolean]) extends Message {
  def apply(a:Agent) = constraingFunc(a)
}