package pl.edu.pw.elka.stud.tkogut.brokering.dialect

case class Entity(name: String) {
  var attributes = Seq[Attribute]()

  override def toString = {
    name + "(" + attributes.mkString(", ") + ")"
  }
}