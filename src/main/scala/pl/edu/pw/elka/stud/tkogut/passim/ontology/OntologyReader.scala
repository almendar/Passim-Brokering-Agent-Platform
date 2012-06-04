package pl.edu.pw.elka.stud.tkogut.passim.ontology

import com.hp.hpl.jena.ontology.OntModel
import com.hp.hpl.jena.rdf.model.ModelFactory
import java.io.{FileInputStream, FileReader, Reader}

import com.hp.hpl.jena.graph.query.Query
import com.hp.hpl.jena.vocabulary.ResultSet
import com.hp.hpl.jena.query.{QueryExecution, ResultSetFormatter, QueryExecutionFactory, QueryFactory}


/**
 * Created by IntelliJ IDEA.
 * User: Tomek
 * Date: 22.04.12
 * Time: 20:19
 * To change this template use File | Settings | File Templates.
 */

class OntologyReader {
    val ontology:OntModel = ModelFactory.createOntologyModel
    private val r = new FileInputStream("system.owl")
    ontology.read(r,null)
  
 }


class DBPediaAccessPoint
{
  val sparqlQueryString1 : String = """
                                      PREFIX dbo: <http://dbpedia.org/ontology/>
                                      PREFIX owl: <http://www.w3.org/2002/07/owl#>
                                      PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
                                      PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
                                      PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
                                      PREFIX foaf: <http://xmlns.com/foaf/0.1/>
                                      PREFIX dc: <http://purl.org/dc/elements/1.1/>
                                      PREFIX : <http://dbpedia.org/resource/>
                                      PREFIX dbpedia2: <http://dbpedia.org/property/>
                                      PREFIX dbpedia: <http://dbpedia.org/>
                                      PREFIX skos: <http://www.w3.org/2004/02/skos/core#>

                                      SELECT ?name    ?birth          ?death ?person WHERE {
                                             ?person  dbo:birthPlace  :Berlin .
                                             ?person  dbo:birthDate   ?birth  .
                                             ?person  foaf:name       ?name   .
                                             ?person  dbo:deathDate   ?death  .
                                              FILTER (?birth < "1900-01-01"^^xsd:date) .
                                      }
                                      ORDER BY ?name"""

  val sparqlQueryString2 : String =
    """
      |                                   PREFIX dbo: <http://dbpedia.org/ontology/>
      |                                      PREFIX owl: <http://www.w3.org/2002/07/owl#>
      |                                      PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
      |                                      PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
      |                                      PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
      |                                      PREFIX foaf: <http://xmlns.com/foaf/0.1/>
      |                                      PREFIX dc: <http://purl.org/dc/elements/1.1/>
      |                                      PREFIX : <http://dbpedia.org/resource/>
      |                                      PREFIX dbpedia2: <http://dbpedia.org/property/>
      |                                      PREFIX dbpedia: <http://dbpedia.org/>
      |                                      PREFIX dbpo: <http://dbpedia.org/ontology/>
      |                                      PREFIX skos: <http://www.w3.org/2004/02/skos/core#>
      |
      |                                      select ?nativeName ?established WHERE {
      |                                             ?uni rdf:type                 dbpo:University.
      |                                             ?uni dbpo:country             :france.
      |                                             ?uni dbpedia2:established     ?established.
      |                                             ?uni dbpedia2:name            ?name.
      |                                             ?uni dbpedia2:nativeName      ?nativeName.
      |
      |
      |
      |                                      }
    """.stripMargin

  val sprqlListCountries =
    """
      |                                   PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
      |                                   PREFIX dbpo: <http://dbpedia.org/ontology/>
      |                                   PREFIX dbpedia2: <http://dbpedia.org/property/>
      |                                   PREFIX foaf: <http://xmlns.com/foaf/0.1/>
      |                                   PREFIX schem: <http://schema.org/>
      |
      |                                   select * WHERE
      |                                   {
      |                                     ?country rdf:type dbpo:Country.
      |                                     ?countr  rdf:type schem:Country.
      |                                     ?uni rdf:type dbpo:University.
      |                                     ?country foaf:name ?name.
      |                                     ?country dbpedia2:capital ?capital.
      |                                     ?uni     dbpo:city ?capital.
      |                                   } LIMIT 1000
    """.stripMargin



  def execute() {
    val query  = QueryFactory.create(sprqlListCountries);
    val qexec  = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);

    val results  = qexec.execSelect();

    ResultSetFormatter.out(System.out, results, query);
    qexec.close() ;
  }

}


object OntologyMain {
  def main(args: Array[String]) {
//    val NS = """http://wizzar.ii.pw.edu.pl/passim-ontology/system.owl#"""
//    val o  = new OntologyReader
//    val chapter = o.ontology.getOntProperty(NS+"studiesAt")
    val db = new DBPediaAccessPoint
    db.execute()
//    val chapterPros = chapter.listProperties
//    while(chapterPros.hasNext) {
//      println(chapterPros.next)
//    }
      

  }
}
