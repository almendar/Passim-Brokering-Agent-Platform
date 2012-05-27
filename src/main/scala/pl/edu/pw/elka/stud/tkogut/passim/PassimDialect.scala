package pl.edu.pw.elka.stud.tkogut.passim

import pl.edu.pw.elka.stud.tkogut.brokering.dialect.{AttributeType, Attribute, Entity, Dialect}
import collection.mutable.ArrayBuffer
import scala.Tuple2

;

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 15.05.12
 * Time: 20:52
 */


class KnowledgeSource(name: String) extends ArrayBuffer[Attribute] {
  override def toString(): String = {
    return name;
  }
}

class QueryData extends ArrayBuffer[Tuple2[Attribute, String]]


object PassimDialect extends Dialect("PassimDialect") {


  final val UNIVERSITY = Entity("University")
  final val universityName = Attribute("UniversityName", AttributeType.STRING);
  final val universityFoundationYear = new Attribute("FoundationYear", AttributeType.DATE);
  final val universityHomeCity = new Attribute("HomeCity", AttributeType.ADDRESS);
  final val universityDepartments = new Attribute("Departamens", AttributeType.STRING, isMultipleValue = true);

  final val universityAttributesList = List(universityName, universityFoundationYear,
    universityHomeCity, universityDepartments)
  UNIVERSITY.addAttributes(universityAttributesList)


  final val PUBLICATION = Entity("Publication")
  final val publicationAuthor = Attribute("PersonName", AttributeType.NAME);
  final val publicationTitle = Attribute("Title", AttributeType.STRING);
  final val publicationISBN = Attribute("ISBN", AttributeType.STRING);
  final val publicationArea = Attribute("PublicationArea", AttributeType.STRING, isMultipleValue = true)
  final val publicationYear = Attribute("PublicationYear", AttributeType.DATE);
  final val publicationIsCitedBy = Attribute("PublicationCited", AttributeType.STRING, isMultipleValue = true)
  final val publicationCitationsNumber = Attribute("PublicationCitationNumber", AttributeType.INTEGER);


  final val publicationAttributesList = List(publicationCitationsNumber, publicationIsCitedBy, publicationYear,
    publicationArea, publicationISBN, publicationTitle, publicationAuthor)
  PUBLICATION.addAttributes(publicationAttributesList)

  final val PERSON = Entity("Person")
  final val personName = publicationAuthor;
  final val personYearsOld = Attribute("PersonYearsOld", AttributeType.INTEGER);
  final val personPhoneNumber = Attribute("PersonPhoneNumber", AttributeType.PHONE_NUMBER);
  final val personEmail = Attribute("PersonEmail", AttributeType.EMAIL)
  final val personGrad = Attribute("PersonGrad", AttributeType.GRAD)
  final val personHomeUniversity = universityName
  final val personHIndex = Attribute("PersonHIndex", AttributeType.INTEGER);

  final val personAttributesList = List(personName, personYearsOld,
    personPhoneNumber, personEmail, personGrad, personHomeUniversity, personHIndex)

  PERSON.addAttributes(personAttributesList)

  append(UNIVERSITY, PUBLICATION, PERSON)
  //val allEntities = university :: publication :: person :: Nil


  val SEARCH_FOR = "?"
  val NO_MATTER = "*"


  def planExecution(entities: Set[Entity], knowledgeBase: List[KnowledgeSource], question: QueryData) {

    val attributesWeLookFor: List[Attribute] =
      question.withFilter(_._2==SEARCH_FOR).map(_._1).toList

      //for (s <- question if s._2 == SEARCH_FOR) yield s._1
    println("We look for:" + attributesWeLookFor.mkString(","))

    val allAttributesSpecified: List[Attribute] = question.map(_._1).toList
    println("Attributes that user gave info on:" + allAttributesSpecified.mkString(", "))

    val possibleSourceOfInformation: List[KnowledgeSource] =
      for (ks: KnowledgeSource <- knowledgeBase;
           attr : Attribute <- attributesWeLookFor;
           if (ks contains attr)
      ) yield ks

    println("Kb may be able to find info for us:" + possibleSourceOfInformation)

    val missingAttributesInKbs: List[List[Attribute]] = possibleSourceOfInformation.map {
      ks: KnowledgeSource =>
        for (attr <- allAttributesSpecified;
             if (!ks.contains(attr))
        ) yield attr
    }

    println("Attributes that are missing in search KBs:" + missingAttributesInKbs)


    val possibleMergeKBs : List[List[KnowledgeSource]] = (possibleSourceOfInformation zip missingAttributesInKbs).map { x =>
       knowledgeBase.filter {
         (k: KnowledgeSource) =>
         val missAttrs: List[Attribute] = x._2
         val checkedKb: KnowledgeSource = x._1
         k!=checkedKb && !missAttrs.intersect(k).isEmpty
       }
    }
   possibleMergeKBs.foreach(x=> println(x.mkString(",")))

   val mergingList: List[(KnowledgeSource, List[KnowledgeSource])] = possibleSourceOfInformation zip possibleMergeKBs

    def merge(knowledgeSources: List[KnowledgeSource], possibleMergesList: List[KnowledgeSource])
    : List[List[KnowledgeSource]] =
    {

      val newKBList : List[KnowledgeSource] =  if (!possibleMergesList.isEmpty)
        knowledgeSources :::List(possibleMergesList.head) else knowledgeSources :::Nil
      val merged: List[Attribute] = newKBList.flatten
      if (allAttributesSpecified.diff(merged).isEmpty)
      {
        return List(newKBList)
      }
      else if(!possibleMergesList.isEmpty && !possibleMergesList.tail.isEmpty)
      {
         merge(newKBList, possibleMergesList.tail)
      }
      else
      {
        return List.empty;
      }
    }

    def startMerge(knowledgeSources: List[KnowledgeSource], possibleMergesList: List[KnowledgeSource])
    : List[List[KnowledgeSource]] =
    {
      if (possibleMergesList.length < 2) merge(knowledgeSources,possibleMergesList)
      else merge(knowledgeSources,possibleMergesList) ::: startMerge(knowledgeSources,possibleMergesList.tail)
    }
    val resolution = mergingList.map { x =>
     startMerge(List(x._1), x._2)
    }

    println(resolution)










  }


  def main(args: Array[String]) {
    val entites = Set(UNIVERSITY, PERSON, PUBLICATION)
    val kb = new KnowledgeSource("KS1") :: new KnowledgeSource("KS2") :: new KnowledgeSource("KS3") :: Nil
    kb(0) ++= List(personName, universityName, universityFoundationYear)
    kb(1) ++= List(universityName, universityHomeCity)
    kb(2) ++= List(universityHomeCity, universityName, universityFoundationYear)
    val question = new QueryData
    question ++= List((personName, SEARCH_FOR), (universityHomeCity, "Warsaw"))
    planExecution(entites, kb, question)
  }

}
