package pl.edu.pw.elka.stud.tkogut.passim.search.bing

import java.net.URL

import scala.collection.mutable.ListBuffer
import java.util.Date
import java.util.Calendar
import java.net.URL
import pl.edu.pw.elka.stud.tkogut.brokering.tools.SingleWebSearchResult
import xml.Node

object BingSearchSingleResult {
  val DATE_TIME_ATTRIBUTE = "DateTime"

  def main(args: Array[String]) {
    val bingGate = new BingSearch("8A4C8362BAF8F435BCF3F8854CBEF493006E398A")
    val results = bingGate.search("gazeta");
    var counter = 0
    for (r <- results) {

      println(counter + ": " + r.mURL)
      counter+=1;

    }
  }

}

class BingSearchSingleResult(url: URL, title: String, description: String, dateTime: Date)
  extends SingleWebSearchResult(url, title, description) {
  additionalAttributes += (BingSearchSingleResult.DATE_TIME_ATTRIBUTE -> dateTime.toString())
  additionalAttributes += ("Microsoft" -> "Bing Engine")

  override def toString(): String = {
    val sb = new StringBuilder(super.toString)
    sb append "\n\tDate: "
    sb append additionalAttributes(BingSearchSingleResult.DATE_TIME_ATTRIBUTE)
    sb.toString
  }
}

class BingSearch(app_id: String) {
  final val BING_URL_START = "http://api.bing.net/xml.aspx?Appid="
  //final val APP_ID = "8A4C8362BAF8F435BCF3F8854CBEF493006E398A"
  private def getSearchURL(query: String): String = BING_URL_START + app_id + "&query=" + query + "&sources=web"

  private val result = ListBuffer[BingSearchSingleResult]()

  def search(lookUpQuery: String): List[BingSearchSingleResult] = {
    result.clear
    val respondXML = getDataFromWeb(lookUpQuery)
    processXMLResponse(respondXML)
    return result.toList
  }

  private def getDataFromWeb(lookUpQuery: String): scala.xml.Elem = {
    val fullQueryAdress = getSearchURL(lookUpQuery)
    val respondXML = scala.xml.XML.load(fullQueryAdress)
    respondXML
  }

  private def processXMLResponse(respondXML: scala.xml.Elem): Unit = {

    def getDateTime(timeText: String) = {
      val searchResultDate: Calendar = Calendar.getInstance();
      val year = (timeText.substring(0, 4)) toInt
      val month = (timeText.substring(5, 7)) toInt
      val day = (timeText.substring(8, 10)) toInt
      val hour = (timeText.substring(11, 13)) toInt
      val minutes = (timeText.substring(14, 16)) toInt;
      val seconds = (timeText.substring(17, 19)) toInt;
      searchResultDate.set(year, month, day, hour, minutes, seconds)
      searchResultDate.getTime()
    }

    val webResults = respondXML \\ "WebResult"
    for (webSingleResult: Node <- webResults) {
      val title = (webSingleResult \ "Title") text
      val description = (webSingleResult \ "Description") text



      val url = (webSingleResult \ "Url")  text


      val timeText = ((webSingleResult \ "DateTime") text)



      val date = getDateTime(timeText)
      val bsr = new BingSearchSingleResult(new URL(url), title, description, date)
      this.result += bsr

    }
  }

}