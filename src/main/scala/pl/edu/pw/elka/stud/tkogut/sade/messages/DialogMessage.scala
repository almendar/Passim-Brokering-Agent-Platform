package pl.edu.pw.elka.stud.tkogut.sade.messages

import pl.edu.pw.elka.stud.tkogut.sade.core.Agent

/**
 * Base for all messages send within a dialog conversation.
 * @param initiator Agent that sends this message
 * @param dialogID  Id of the dialog
 */
abstract case class DialogMessage(initiator: Agent, dialogID: String) extends Message