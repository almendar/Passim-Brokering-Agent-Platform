package pl.edu.pw.elka.stud.tkogut.sade.messages

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 06.05.12
 * Time: 17:10
 * To change this template use File | Settings | File Templates.
 */

/**
 * Message purpose is to signal Agent that he should checkout this dialog. Two options, if dialog was created
 * by this agent he should send to other Agent message of the same type to keep it away from timeout.
 * If this dialog was initiated by the other agent it should update dialog touch time.
 * @param dialogId dialog id
 */
case class KeepAliveMessage(dialogId:String) extends Message
