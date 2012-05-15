package pl.edu.pw.elka.stud.tkogut.brokering.dialect

import AttributeType._

class BoundAttribute[X](name: String, attrType: AttributeType)
  extends Attribute(name, attrType) {

  private var _constraints: Function[X,Boolean] = null

  def constraints[X] = _constraints

  def constraints_(values: Set[X]) = _constraints = values

  def validateValue(value: X): Option[Boolean] = {
    constraints match {
      case null => None
      case _ => Option(constraints(value))
    }
  }
}

