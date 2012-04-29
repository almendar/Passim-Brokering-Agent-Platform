package pl.edu.pw.elka.stud.tkogut.sade.messages

import pl.edu.pw.elka.stud.tkogut.sade.core.Agent

/**
 *
 * @param from Source agent
 * @param constraintFunc  Function takes an agent and says if caller is interested in this agent.
 */

case class SendAgentsMeetingConstraint(from:Agent, constraintFunc: Function1[Agent, Boolean]) extends Message {
  def apply(a:Agent) = constraintFunc(a)
}