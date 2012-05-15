package pl.edu.pw.elka.stud.tkogut.brokering.dialect

import collection.mutable.Buffer

case class Entity(name: String) {
  var attributes = Buffer[Attribute]()

  def addAttribute(attribute:Attribute): Entity = {
    attributes+=attribute
    return this
  }
  override def toString = {
    name + "(" + attributes.mkString(", ") + ")"
  }
}