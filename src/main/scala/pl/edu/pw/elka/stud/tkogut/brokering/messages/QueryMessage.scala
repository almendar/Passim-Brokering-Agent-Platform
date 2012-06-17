package pl.edu.pw.elka.stud.tkogut.brokering.messages

import pl.edu.pw.elka.stud.tkogut.sade.messages._
import pl.edu.pw.elka.stud.tkogut.sade.core.Agent
import pl.edu.pw.elka.stud.tkogut.brokering.dialect.Attribute
import collection.mutable.ArrayBuffer

/**
 * Class is for sending queries in free text to search enginees that don't
 * support Dialects.
 * @param sender This agent
 * @param dialogId ID of the dialogs
 * @param query Query text
 */
case class QueryMessage(sender:Agent,
    dialogId: String, query:String) extends DialogMessage(sender,dialogId)

/**
 * Class for sending structuralized queries that comply with accepted Dialect
 * @param sender This agent.
 * @param dialogId ID of the dialog.
 */
case class EntityQueryMessage(sender:Agent, dialogId: String) extends DialogMessage(sender,dialogId)
{
  private val constraints =  ArrayBuffer[Tuple2[Attribute,(String=>Boolean)]]()
  private final var searchFors = ArrayBuffer[Attribute]()

  /**
   * Add new constraint on the search.
   * @param param Attribute on wchich constraint will be set.
   * @param func Function that computes if this is what we're looking for.
   */
  def addQueryAttribute(param:Attribute, func:(String)=>(Boolean) )
  {
    constraints += param->func;
  };

  def addSearchAttribute(param:Attribute)
  {
    searchFors +=  param

  }

  def addDisplayAttribute(param:Attribute) {
    val func = (arg:String) => (true)
    constraints += param->func
  }
}