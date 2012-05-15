package pl.edu.pw.elka.stud.tkogut.passim

import pl.edu.pw.elka.stud.tkogut.brokering.dialect.{AttributeType, Attribute, Entity, Dialect,BoundAttribute}


/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 15.05.12
 * Time: 20:52
 * To change this template use File | Settings | File Templates.
 */

object PassimDialect extends Dialect("PassimDialect") {

  private var universityName = Attribute("name",AttributeType.STRING);
  private var foundationYear = new BoundAttribute[Int]("FoundationYear", AttributeType.INTEGER);
  //foundationYear.constraints = new Function1[Int,Boolean] {override def apply(arg:Int) = {(arg < 2100) && (arg > 500)}}

  private var university = Entity("University")
  university.addAttribute(universityName).addAttribute(foundationYear)
}
