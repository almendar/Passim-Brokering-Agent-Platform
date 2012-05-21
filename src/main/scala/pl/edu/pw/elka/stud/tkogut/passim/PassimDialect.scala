package pl.edu.pw.elka.stud.tkogut.passim

import pl.edu.pw.elka.stud.tkogut.brokering.dialect.{AttributeType, Attribute, Entity, Dialect}

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 15.05.12
 * Time: 20:52
 */

object PassimDialect extends Dialect("PassimDialect")
{

  final val UNIVERSITY = Entity("University")
  final val universityName = Attribute("Name", AttributeType.STRING);
  final val universityFoundationYear = new Attribute("FoundationYear", AttributeType.DATE);
  final val universityHomeCity = new Attribute("HomeCity",AttributeType.ADDRESS);
  final val universityDepartments = new Attribute("Departamens", AttributeType.STRING, isMultipleValue = true);

  final val universityAttributesList = List(universityName,universityFoundationYear,
    universityHomeCity,universityDepartments)
  UNIVERSITY.addAttributes(universityAttributesList)



  final val PUBLICATION = Entity("Publication")
  final val publicationAuthor = Attribute("Name",AttributeType.NAME);
  final val publicationTitle  = Attribute("Title",AttributeType.STRING);
  final val publicationISBN =   Attribute("ISBN",AttributeType.STRING);
  final val publicationArea = Attribute("PublicationArea", AttributeType.STRING,isMultipleValue=true)
  final val publicationYear = Attribute("PublicationYear", AttributeType.DATE);
  final val publicationIsCitedBy = Attribute("PublicationCited", AttributeType.STRING,isMultipleValue=true)
  final val publicationCitationsNumber = Attribute("PublicationCitationNumber",AttributeType.INTEGER);


  final val publicationAttributesList = List(publicationCitationsNumber,publicationIsCitedBy,publicationYear,
    publicationArea,publicationISBN,publicationTitle,publicationAuthor)
  PUBLICATION.addAttributes(publicationAttributesList)

  final val PERSON = Entity("Person")
  final val personName = publicationAuthor;
  final val personYearsOld = Attribute("PersonYearsOld",AttributeType.INTEGER);
  final val personPhoneNumber = Attribute("PersonPhoneNumber",AttributeType.PHONE_NUMBER);
  final val personEmail = Attribute("PersonEmail", AttributeType.EMAIL)
  final val personGrad = Attribute("PersonGrad", AttributeType.GRAD)
  final val personHomeUniversity = universityName
  final val personHIndex = Attribute("PersonHIndex", AttributeType.INTEGER);

  final val personAttributesList = List(personName,personYearsOld,
    personPhoneNumber,personEmail,personGrad,personHomeUniversity,personHIndex)

  PERSON.addAttributes(personAttributesList)

  append(UNIVERSITY,PUBLICATION,PERSON)
  //val allEntities = university :: publication :: person :: Nil


  val SEARCH_FOR = "?"
  val NO_MATTER = "*"


  def planExecution(entites: List[Entity], kb: List[List[Attribute]], question: List[(Attribute,String)])
  {
        val attributesWeLookFor = for(s <- question if s._2==SEARCH_FOR) yield s
        val allAtributesSpecified = question.map(_._1)
        val ourSrcOfInformation = kb.distinct.filter(attributesWeLookFor.contains(_));
        for
        {
          src <- ourSrcOfInformation
          oth <-kb
          if (oth.con)
        }

  }



  def main(args: Array[String])
  {
    val entites = List(UNIVERSITY,PERSON,PUBLICATION)
    val kb = List();
        (
      List(),
      List(),
      List(),

    )

    val question : List[(Attribute,String)] = List[(Attribute,String)]
    (
      (personName,SEARCH_FOR) ,
      (universityHomeCity,"Warsaw")
    )
    planExecution(entites,kb,question)
    val msg = "Passim dialect\n%s".format(PassimDialect)
    println(msg)
  }

}
