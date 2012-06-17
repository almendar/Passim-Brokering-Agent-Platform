package pl.edu.pw.elka.stud.tkogut.brokering.dialect


import collection.mutable.ListBuffer

object AttributeType extends Enumeration {
  type AttributeType = Value
  val NAME, ADDRESS, PHONE_NUMBER, EMAIL, GRAD, TIME, DATE, DATETIME,
  INTEGER, FLOAT, STRING = Value
}

import AttributeType._

object Attribute {
  final val SEARCH_FOR = "?"
  final val NO_MATTER = "*"
}

case class Attribute(name: String, attrType: AttributeType, isMultipleValue:Boolean = false) {

  private val parentEntities = new ListBuffer[Entity]

  def registerEntity(entity:Entity) {
    parentEntities.append(entity)
  }

  //  def  = match(value) {
  //    case String => STRING
  //    case Integer => INTEGER
  //    case Float => FLOAT
  //    case Date => DATE
  override def toString = name + ":" + attrType
}

/**
 * Object describing search agents that can use free-text search.
 */
object FreeTextSearch extends Attribute("FreeText",AttributeType.STRING,false)
//  def this(name: String, value: String) = this(name, value, STRING)
 // def this(name: String, value: Integer) = this(name, value, INTEGER)
 // def this(name: String, value: Float) = this(name, value, FLOAT)
 // def this(name: String, value: Date) = this(name, value, DATE)

