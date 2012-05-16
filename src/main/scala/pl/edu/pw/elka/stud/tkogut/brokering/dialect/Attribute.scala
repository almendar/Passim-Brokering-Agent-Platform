package pl.edu.pw.elka.stud.tkogut.brokering.dialect


import java.util.Date
import collection.mutable.ListBuffer

object AttributeType extends Enumeration {
  type AttributeType = Value
  val TIME, DATE, DATETIEM, INTEGER, FLOAT, STRING = Value
}

import AttributeType._

case class Attribute(name: String, attrType: AttributeType) {

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
//  def this(name: String, value: String) = this(name, value, STRING)
 // def this(name: String, value: Integer) = this(name, value, INTEGER)
 // def this(name: String, value: Float) = this(name, value, FLOAT)
 // def this(name: String, value: Date) = this(name, value, DATE)

