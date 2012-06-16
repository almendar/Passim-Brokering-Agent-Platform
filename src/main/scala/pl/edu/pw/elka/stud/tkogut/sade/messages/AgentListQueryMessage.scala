package pl.edu.pw.elka.stud.tkogut.sade.messages
import pl.edu.pw.elka.stud.tkogut.sade.core.Agent

/**
 * Message send from @see{YellowPagesAgent} with list of agents.
 * It is send as a reply to @see{SendAgentsMeetingConstraint}
 * @param list List of agents
 */
case class AgentListQueryMessage(list: List[Agent]) extends Message