package pl.edu.pw.elka.stud.tkogut.sade.core

import java.util.UUID
import collection.mutable.HashMap

/**
 * Created with IntelliJ IDEA.
 * User: tomek
 * Date: 11.06.12
 * Time: 20:16
 * To change this template use File | Settings | File Templates.
 */


object DialogManager {
  final def generateID(): String = {
    UUID.randomUUID().toString()
  }
}

/**
 *
 * @param agent Agent whose dialogs will be managed.
 */
class DialogManager(agent:Agent) {

  protected[core] val activeDialogs = new HashMap[String, Dialog]

  /**
   * Find all dialogs that are in timeout state.
   * @return Iterable with ids that are from outdated dialogs.
   */
  def getOutdatedDialogs() : Iterable[String] = {
    val currentTime = System.currentTimeMillis()
    val ret = activeDialogs.values.filter{ p:Dialog =>
      val dt = (currentTime - p._updateTime)
      val isTimedOut = (dt > Dialog.DIALOG_TIMEOUT)
      isTimedOut && !p.isInitiated
    }.map(_.id)
    return ret
  }

  /**
   * Finds all dialogs that were initiated by this agent and
   * should have a KeepAliveMessage send.
   * @return Iterable with list of ids of dialogs that have they time
   * beyond @see{Dialog::&KEEP_ALIVE_TIME}
   */
  def getDialogsThatShouldBeUpdated() : Iterable[String] = {
    val currentTime = System.currentTimeMillis()
    val ret = activeDialogs.values.filter{ p:Dialog =>
    val dt = (currentTime - p._updateTime)
    val isTimedOut = dt > Dialog.KEEP_ALIVE_TIME //send update if half of the time has passed
    isTimedOut && p.isInitiated
    }.map(_.id)
    return ret
  }

  def establishResponseDialog(withAgent:Agent,dialogId:String) = {
    val d = new Dialog(withAgent, dialogId)
    activeDialogs += (dialogId -> d);
  }

  def updateTime(id:String) {
    assert(activeDialogs(id).isInitiated)
    activeDialogs(id).updateTime()
  }

  def establishDialog(withAgent:Agent) : String = {
    val dialogId = DialogManager.generateID()
    val d = new Dialog(withAgent, dialogId,isInitiated = true)
    activeDialogs += (dialogId -> d);
    return dialogId
  }


  def invokeAction(id:String) {
    activeDialogs(id).nextAction(id)
  }

  def setDialogNextAction(id:String, action:(String) => (Unit)) {
    activeDialogs(id).nextAction = action
  }

  def isConfirmed(dialogId:String) : Boolean = {
    return activeDialogs(dialogId).isConfirmed
  }

  def confirmDialog(dialogId:String) {
    val adress = activeDialogs(dialogId).contact
    adress ! (Agent.OK, dialogId)
  }

  def endDialogWithAgent(dialogId:String) {
    val adress = activeDialogs(dialogId).contact
    agent.speak("Ending dialog with:" + adress)
    removeDialog(dialogId)
    adress ! (Agent.BYE, dialogId)
  }

  def removeDialog(dialogId:String) {
    activeDialogs -= dialogId

  }

  def getContact(dialogId:String)  : Agent ={
    activeDialogs(dialogId).contact
  }

  def setDialogConfirmed(dialogId:String) {
    activeDialogs(dialogId).isConfirmed = true
  }


  def hasDialog(id:String) : Boolean = {
      return activeDialogs.contains(id)
  }

  def getAttribute(id:String,key:String) : Object = {
     return activeDialogs(id).attributes(key)
  }

  def putAttribute(id:String, key:String, obj:Object) = {
    activeDialogs(id).attributes(key) = obj
  }

  def putAttribute(id:String, pair:(String,Object) ) {
    activeDialogs(id).attributes+=pair
  }

}
