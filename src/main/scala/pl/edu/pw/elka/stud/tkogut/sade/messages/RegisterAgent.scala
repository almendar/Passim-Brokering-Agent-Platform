package pl.edu.pw.elka.stud.tkogut.sade.messages

import pl.edu.pw.elka.stud.tkogut.sade.core.yellowpages.YellowPagesSearchable

/**
 * Message send by agent that implements @see{YellowPagesSearchable} to
 * YellowPages agent.
 * @param from Reference of agent that want's to register.
 * @param description String with description of the agent.
 */
case class RegisterAgent(from: YellowPagesSearchable, description: String) extends Message