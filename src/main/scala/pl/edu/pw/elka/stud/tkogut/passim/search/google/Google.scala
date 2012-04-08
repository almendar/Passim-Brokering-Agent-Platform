package pl.edu.pw.elka.stud.tkogut.passim.search.google
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URLConnection
import java.net.URL
import scala.collection.mutable.ListBuffer
import pl.edu.pw.elka.stud.tkogut.brokering.tools._
import net.liftweb.json.JArray
import net.liftweb.json.JsonAST._

//My API key to google search
//AIzaSyCZ72XdhT4SOG2BUdGFA043jNxT9Fd4wPk

case class GoogleSearchSingleResult(url: URL, title: String, description: String)
  extends SingleWebSearchResult(url, title, description) {
  additionalAttributes += ("Google" -> "Search Engine")
}

object GoogleSearch {

  private val LINK_URL_MAP_KEY = "link"
  private val TITLE_MAP_KEY = "title"
  private val DESCRIPTION = "snippet"
  private val JSON_DOC_KEY_SEARCH_RESULTS = "items"
}

class GoogleSearch(apiKey: String) {

  private val result = new ListBuffer[GoogleSearchSingleResult]

  def search(query: String): List[GoogleSearchSingleResult] = {
    result.clear
    setQuery(query)
    getJsonResponse
    parseJSONDocument
    result.toList
  }

  //var j: JArray = new JArray("qwe", null)

  /*for {
      JField("items", JArray(value)) <- jsonDocumentParsed

    } yield*/

  /*
    var k = 0
    for (i <- a(0)) {
    	println(k + ". " + i + "\n")
      k += 1
    }
    */

  def processGoogleJSON(jsonDoc: String) = {
    this.jsonRespones = jsonDoc
    parseJSONDocument
    result.toList
  }

  /**
   * This method constructs http query to google that will provide json-format answer
   */
  private def setQuery(query: String) = {
    val queryReplaced = query.replace(" ", "+")
    httpQuery = "https://www.googleapis.com/customsearch/v1?key=" + apiKey + "&cx=013036536707430787589:_pqjad5hr1a&q=" + queryReplaced + "&alt=json"
  }

  private def getJsonResponse() = {
    val urlGoogleSearch = new java.net.URL(httpQuery)
    val connection: URLConnection = urlGoogleSearch.openConnection
    val in = new BufferedReader(new InputStreamReader(connection.getInputStream));
    var tmpLine: String = in.readLine()
    val jsonDocumentSB = new StringBuilder
    while (tmpLine != null) {
      jsonDocumentSB.append(tmpLine)
      jsonDocumentSB.append("\n")
      tmpLine = in.readLine()
    }
    in.close();
    jsonRespones = jsonDocumentSB.mkString
    //println(jsonRespones)
  }

  /**
   * Hold what Google has send back
   */
  protected var jsonRespones: String = null
  private var httpQuery: String = null
  private var jsonDocumentParsed: JValue = null

  private def parseJSONDocument: Unit = {
    jsonDocumentParsed = net.liftweb.json.parse(jsonRespones)
    val items = jsonDocumentParsed.filter(
      s => s match {
        case JField(GoogleSearch.JSON_DOC_KEY_SEARCH_RESULTS, JArray(value)) =>
          true
        case h =>
          false
      })

    val jarrayOfResults: JArray = if (items.length > 0) items(0).asInstanceOf[JField].value.asInstanceOf[JArray] else JArray(List())
    val resultsMap = jarrayOfResults.values.asInstanceOf[List[Map[String, String]]]
    for (l <- resultsMap) {
      val url: URL = new URL(l(GoogleSearch.LINK_URL_MAP_KEY))
      val title: String = if (!l.getOrElse(GoogleSearch.TITLE_MAP_KEY, "").trim().equals("")) l(GoogleSearch.TITLE_MAP_KEY) else SingleSearchResult.MISSING_TITLE
      val desc: String = if (!l(GoogleSearch.DESCRIPTION).trim().equals("")) l(GoogleSearch.DESCRIPTION) else SingleSearchResult.MISSING_DESCRIPTION
      val singleResult = new GoogleSearchSingleResult(url, title, desc)
      result.append(singleResult)
    }
  }

}