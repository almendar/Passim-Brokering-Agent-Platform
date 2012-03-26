package pl.edu.pw.elka.stud.tkogut.brokering.dialect
import AttributeType._

class BoundAttribute(name: String, attrType: AttributeType)
  extends Attribute(name, attrType) {

  private var _constraints: Set[Any] = null

  def constraints = _constraints
  def constraints_(values: Set[Any]) = _constraints = values
  
  def validateValue(value: Any): Option[Boolean] = {
    constraints match {
      case null => None
      case _ => Option(constraints.contains(value))
    }
  }
}