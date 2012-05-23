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


class KnowledgeSource(name:String) extends ArrayBuffer[Attribute] {
  override def toString() : String =
  {
    return name;
  }
}


class QueryData extends ArrayBuffer[Tuple2[Attribute,String]]



object PassimDialect extends Dialect("PassimDialect")
{


  final val UNIVERSITY = Entity("University")
  final val universityName = Attribute("UniversityName", AttributeType.STRING);
  final val universityFoundationYear = new Attribute("FoundationYear", AttributeType.DATE);
  final val universityHomeCity = new Attribute("HomeCity",AttributeType.ADDRESS);
  final val universityDepartments = new Attribute("Departamens", AttributeType.STRING, isMultipleValue = true);

  final val universityAttributesList = List(universityName,universityFoundationYear,
    universityHomeCity,universityDepartments)
  UNIVERSITY.addAttributes(universityAttributesList)



  final val PUBLICATION = Entity("Publication")
  final val publicationAuthor = Attribute("PersonName",AttributeType.NAME);
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


  def planExecution(entites: Set[Entity], kb: Array[KnowledgeSource], question:QueryData)
  {

        val attributesWeLookFor = for(s <- question if s._2==SEARCH_FOR) yield s._1

        println("We look for:" + attributesWeLookFor.mkString(","))
        val allAtributesSpecified = question.map(_._1)
        println("Attributes that user gave info on:" + allAtributesSpecified.mkString(","))
        val ourSrcOfInformation: List[KnowledgeSource] =
          for(ks <- kb.toList;
              attr <- attributesWeLookFor;
              if(ks contains attr)
            ) yield ks

        println("Kb may be able to find info for us:" + ourSrcOfInformation)

        val missingAttributesInKbs: List[ArrayBuffer[Attribute]] = ourSrcOfInformation.map
        {
          ks:KnowledgeSource =>
            for(a <- allAtributesSpecified;
              if(!ks.contains(a))
            ) yield a
        }

        println("Attributes that are missing in search KBs:"+missingAttributesInKbs)

    val otherKbsThatCanBeUsed: List[Array[KnowledgeSource]] =
      (ourSrcOfInformation zip missingAttributesInKbs).map {
        para:(KnowledgeSource, ArrayBuffer[Attribute]) =>
          val r = for(
            kbs:KnowledgeSource <- kb;
            if (para._1!=kbs) && (para._2.filter(kbs.contains(_)).nonEmpty)
          ) yield kbs
          println(r)
          r
      }
      println("Other KBs that can help:"+otherKbsThatCanBeUsed(0).toString)

//          map {
//           (  =>
//              for (
//                kbs <- kb;
//                p <- para;
//                a <- p._2
//                if(p._1 != kbs)
//
//              ) yield kbs
//        }


    /*
          kb.filter
        {
          (ks : Set[Attribute]) =>
          {
            !((attributesWeLookFor & ks).isEmpty)
          }
        }.toList


        val missingAttributes = ourSrcOfInformation.map{
           allAtributesSpecified &~ _
        }
        println("Sources need additional atributes"+missingAttributes)

        ourSrcOfInformation.map { l:List[defs.KnowledgeSource] =>
                 l
        }

      */
        /*for
        {
          src <- ourSrcOfInformation
          oth <-kb
          if (oth.con)
        } */

  }



  def main(args: Array[String])
  {
    val entites = Set(UNIVERSITY,PERSON,PUBLICATION)

    val kb = Array(new KnowledgeSource("KS1"), new KnowledgeSource("KS2"),new KnowledgeSource("KS3"))
    kb(0)++=List(personName,universityName,universityFoundationYear)
    kb(1)++=List(universityName,universityHomeCity)
    kb(2)++=List(universityHomeCity,universityName, universityFoundationYear)
    val question  = new QueryData
    question++=List((personName,SEARCH_FOR), (universityHomeCity,"Warsaw"))
    planExecution(entites,kb,question)



   /*
    (
      )




    val msg = "Passim dialect\n%s".format(PassimDialect)
    println(msg)
    */
  }

}