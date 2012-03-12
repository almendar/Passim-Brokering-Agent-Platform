package pl.edu.pw.elka.stud.tkogut.passim.websearchers
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import java.io.File
import scala.io.Source
import java.io.IOException
import pl.edu.pw.elka.stud.tkogut.passim.sade.core.yellowpages.YellowPagesAgent
import pl.edu.pw.elka.stud.tkogut.passim.search._

class GoogleSearchJsonTest extends FunSuite with BeforeAndAfter {
  val testFileName = "jsonGoogleSearchResultTest.txt"
  var jsonResponeString: String = null
  val API_KEY = "AIzaSyCZ72XdhT4SOG2BUdGFA043jNxT9Fd4wPk"
  before {
    //YellowPagesAgent.book.clear()
    val f = new File("brokering/src/test/resources/" + testFileName)
    jsonResponeString = Source.fromFile(f).mkString
  }

  test("Trying downloading from google") {
    val searchTerms = List("InnerValue", "mp3", "Linux", "Computer science")
    for (term <- searchTerms) {
      val gs = new GoogleSearch(API_KEY)
      try {
        val results = gs.search(term)
      } catch {
        case e: java.io.IOException =>
          val msg: String = e.getMessage()
          if (!(msg contains "Server returned HTTP response code: 403"))
            throw e
      }

    }
  }

  test("Parsing offline JSON doc from google") {
    val gs = new GoogleSearch(API_KEY)
    val results = gs.processGoogleJSON(jsonResponeString)
    val r1: GoogleSearchSingleResult = results(0)
    assert(r1.title == "IMDb - Se7en (1995)")
    assert(r1.description == "Directed by David Fincher. With Morgan Freeman, Brad Pitt, Kevin Spacey,   Gwyneth Paltrow. Two detectives, a rookie and a veteran, hunt a serial killer who ...")
    assert(r1.url.toString == "http://www.imdb.com/title/tt0114369/")
  }

}