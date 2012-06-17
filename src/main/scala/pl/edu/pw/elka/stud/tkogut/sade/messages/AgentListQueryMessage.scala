package pl.edu.pw.elka.stud.tkogut.sade.messages
import pl.edu.pw.elka.stud.tkogut.sade.core.Agent
import pl.edu.pw.elka.stud.tkogut.brokering.SearchAgent

/**
 * Message send from @see{YellowPagesAgent} with list of agents.
 * It is send as a reply to @see{SendAgentsMeetingConstraint}
 */
class AgentListQueryMessage(listOfAgents:Iterable[Agent]) extends Message
{
  private val list: List[Agent] = listOfAgents.toList

  def getAgents(): Iterable[Agent] = {
    list.toIterable
  }

  def getNumberOfAgents() : Int = {
    return list.length
  }
}