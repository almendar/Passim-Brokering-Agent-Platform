package pl.edu.pw.elka.stud.tkogut.passim

import pl.edu.pw.elka.stud.tkogut.brokering.dialect.{AttributeType, Attribute, Entity, Dialect, BoundAttribute}

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 15.05.12
 * Time: 20:52
 */

object PassimDialect extends Dialect("PassimDialect") {

  final val UNIVERSITY = Entity("University")
  final val PUBLICATION = Entity("Publication")
  final val PERSON = Entity("Person")

  append(UNIVERSITY,PUBLICATION,PERSON)

  private val universityName = Attribute("name", AttributeType.STRING);
  private val universityFoundationYear = new BoundAttribute[Int]("foundationYear", AttributeType.INTEGER);
  universityFoundationYear.constraints = new Function1[Int, Boolean] {
    override def apply(arg: Int) = {
      (arg < 2100) && (arg > 500)
    }
  }
  UNIVERSITY.addAttributes(universityName, universityFoundationYear)

  private val personName = Attribute("name", AttributeType.STRING)
  PERSON.addAttributes(personName)

  private val publicationName = Attribute("name", AttributeType.STRING)
  private val publicationYear = new BoundAttribute("year", AttributeType.INTEGER)
  publicationYear.constraints = new Function1[Int, Boolean] {
    override def apply(arg: Int) = {
      arg < 2100 && arg > 1700
    }
  }

  PUBLICATION.addAttributes(publicationName, publicationYear, personName)

  //val allEntities = university :: publication :: person :: Nil


  def main(args: Array[String]) {
    val msg = "Passim dialect\n%s".format(PassimDialect)
    println(msg)
    println(PassimDialect.UNIVERSITY.attributes)
    PassimDialect.PUBLICATION

  }

}
