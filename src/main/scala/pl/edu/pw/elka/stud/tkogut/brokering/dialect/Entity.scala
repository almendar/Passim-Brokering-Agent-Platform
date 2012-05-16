package pl.edu.pw.elka.stud.tkogut.brokering.dialect

import collection.mutable.Buffer

case class Entity(name: String) {
  var attributes = Buffer[Attribute]()

  def addAttributes(attr: Attribute*): Entity = {
    for (a <- attr) {
      attributes += a
      a.registerEntity(this)
    }
    return this
  }
  override def toString = {
    name + "(" + attributes.mkString(", ") + ")"
  }
}