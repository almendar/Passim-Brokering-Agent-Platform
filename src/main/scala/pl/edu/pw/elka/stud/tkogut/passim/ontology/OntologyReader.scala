package pl.edu.pw.elka.stud.tkogut.passim.ontology

import com.hp.hpl.jena.ontology.OntModel
import com.hp.hpl.jena.rdf.model.ModelFactory
import java.io.{FileInputStream, FileReader, Reader}


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


object OntologyMain {
  def main(args: Array[String]) {
    val NS = """http://wizzar.ii.pw.edu.pl/passim-ontology/system.owl#"""
    val o  = new OntologyReader
    val chapter = o.ontology.getOntProperty(NS+"studiesAt")
//    val chapterPros = chapter.listProperties
//    while(chapterPros.hasNext) {
//      println(chapterPros.next)
//    }
      

  }
}
