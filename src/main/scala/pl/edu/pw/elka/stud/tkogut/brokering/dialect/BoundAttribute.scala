package pl.edu.pw.elka.stud.tkogut.brokering.dialect

import AttributeType._

class BoundAttribute[X](name: String, attrType: AttributeType)
  extends Attribute(name, attrType) {

  var constraints: Function1[X, Boolean] = null

  //def constraints[X] = constraints

  def validateValue(value: X): Option[Boolean] = {
    constraints match {
      case null => None
      case _ => Option(constraints(value))
    }
  }
}

