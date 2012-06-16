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
  /**
   * Generates unique identyfier for dialog.
   * @return Unique id
   */
  final def generateID(): String = {
    UUID.randomUUID().toString()
  }
}

/**
 * Manages dialogs of an Agent.
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

  /**
   * Creates new dialogs in response to invocations from other agent.
   * @param withAgent Agent that started the dialog.
   * @param dialogId Id of the dialogs.
   */
  def establishResponseDialog(withAgent:Agent,dialogId:String) {
    val d = new Dialog(withAgent, dialogId)
    activeDialogs += (dialogId -> d);
  }

  /**
   * Updates timestamp of dialogs. Should be only used on
   * dialogs initiated by this agent.
   * @param id Id of the dialog.
   */
  def updateTime(id:String) {
    assert(activeDialogs(id).isInitiated)
    activeDialogs(id).updateTime()
  }

  /**
   * Used to create new dialog that is initiated by this agent.
   * @param withAgent Adress of the agent this agent want to dialog with.
   * @return Id of the newly created dialog.
   */
  def establishDialog(withAgent:Agent) : String = {
    val dialogId = DialogManager.generateID()
    val d = new Dialog(withAgent, dialogId,isInitiated = true)
    activeDialogs += (dialogId -> d);
    return dialogId
  }


  /**
   * Invokes callback function that was registered with this dialog.
   * @param id Id of the dialogs where function is registered.
   */
  def invokeAction(id:String) {
    activeDialogs(id).nextAction(id)
  }

  /**
   * Registers callback function
   * @param id Id of the dialog where function should be registered.
   * @param action Function to register.
   */
  def setDialogNextAction(id:String, action:(String) => (Unit)) {
    activeDialogs(id).nextAction = action
  }

  /**
   * Tells if dialogs was confirmed.
   * @param dialogId Id of the dialog.
   * @return If dialog was confirmed.
   */
  def isConfirmed(dialogId:String) : Boolean = {
    return activeDialogs(dialogId).isConfirmed
  }

  /**
   * Set dialogs as confirmed and send message @see{Agent.OK} to agent
   * associated with this dialogs.
   * @param dialogId Id of the dialog.
   */
  def confirmDialog(dialogId:String) {
    val adress = activeDialogs(dialogId).contact
    adress ! (Agent.OK, dialogId)
  }

  /**
   * Removes dialogs from internal structure without sending message.
   * @param dialogId
   */
  def removeDialog(dialogId:String) {
    activeDialogs -= dialogId
  }

  /**
   * Ends dialogs with agent by deleting internal structures
   * and send a message @see{Agent.BYE} to agent from the dialog.
   * @param dialogId Dialogs to be ended.
   */
  def endDialogWithAgent(dialogId:String) {
    val adress = activeDialogs(dialogId).contact
    agent.speak("Ending dialog with:" + adress)
    removeDialog(dialogId)
    adress ! (Agent.BYE, dialogId)
  }



  /**
   * Gets contact of the dialog
   * @param dialogId ID of the dialogs
   * @return Reference to dialog contact
   */
  def getContact(dialogId:String)  : Agent ={
    activeDialogs(dialogId).contact
  }

  /**
   * Sets dialog as confirmed.
   * @param dialogId
   */
  def setDialogConfirmed(dialogId:String) {
    activeDialogs(dialogId).isConfirmed = true
  }

  /**
   * Checks if dialog of given identyfier was created.
   * @param id Id of the dialogs.
   * @return True is dialogs exist, false otherwise.
   */
  def hasDialog(id:String) : Boolean = {
      return activeDialogs.contains(id)
  }

  /**
   * Gets attribute of dialog.
   * @param id Dialog id
   * @param key Key of the attribute under which it was stored.
   * @return Object saved under given key.
   */
  def getAttribute(id:String,key:String) : AnyRef = {
     return activeDialogs(id).attributes(key)
  }

  /**
   * Associates an object under a given key with dialog.
   * @param id Id of the dialogs where object is to be put.
   * @param key Key under which object will be stored.
   * @param obj Object that will be stored.
   */
  def putAttribute(id:String, key:String, obj:AnyRef) = {
    activeDialogs(id).attributes(key) = obj
  }

  /**
   * Associates an object under a given key with dialog passed as a pair.
   * @param id Id of the dialogs where object is to be put.
   * @param pair Pair of objects to be stored.
   */
  def putAttribute(id:String, pair:(String,Object) ) {
    activeDialogs(id).attributes+=pair
  }

}
