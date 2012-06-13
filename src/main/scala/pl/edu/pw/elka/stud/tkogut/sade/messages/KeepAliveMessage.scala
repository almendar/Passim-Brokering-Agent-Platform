package pl.edu.pw.elka.stud.tkogut.sade.messages

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 06.05.12
 * Time: 17:10
 * To change this template use File | Settings | File Templates.
 */

/**
 * Message purpose is to signal Agent that he should update time of this dialog not to
 * get deleted on next timeout check.
 * @param dialogId dialog id
 */
case class KeepAliveMessage(dialogId:String) extends Message
