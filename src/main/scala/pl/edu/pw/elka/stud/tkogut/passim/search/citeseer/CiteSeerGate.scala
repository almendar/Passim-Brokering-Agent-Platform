package pl.edu.pw.elka.stud.tkogut.passim.search.citeseer

import java.net.Socket
import org.apache.http.util.EntityUtils
import org.apache.http.params.{HttpProtocolParams, SyncBasicHttpParams, BasicHttpParams, HttpParams}
import org.apache.http._
import client.entity.UrlEncodedFormEntity
import client.utils.URLEncodedUtils
import entity.{StringEntity, ByteArrayEntity, InputStreamEntity}
import impl.{DefaultConnectionReuseStrategy, DefaultHttpClientConnection}
import message.{BasicNameValuePair, BasicHttpEntityEnclosingRequest, BasicHttpRequest}
import protocol._
import java.io.ByteArrayInputStream
import xml.{Node, XML}
import scala.util.matching.Regex.{MatchIterator, Match}
import scala.collection.JavaConversions._
import scala.collection.mutable.{Map, ArrayBuffer}


/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 30.05.12
 * Time: 20:46
 * To change this template use File | Settings | File Templates.
 */


 class CiteSeerHtmlDownloader
 {
   val params: HttpParams = new SyncBasicHttpParams();
   HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
   HttpProtocolParams.setContentCharset(params, "UTF-8");
   HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");
   HttpProtocolParams.setUseExpectContinue(params, true);
   val httpproc: HttpProcessor = new ImmutableHttpProcessor(Array[HttpRequestInterceptor](
     // Required protocol interceptors
     new RequestContent(),
     new RequestTargetHost(),
     // Recommended protocol interceptors
     new RequestConnControl(),
     new RequestUserAgent(),
     new RequestExpectContinue()));
   val httpexecutor: HttpRequestExecutor = new HttpRequestExecutor();
   val context = new BasicHttpContext(null);
   val host = new HttpHost("citeseerx.ist.psu.edu", 80);
   val conn = new DefaultHttpClientConnection();
   val connStrategy = new DefaultConnectionReuseStrategy();
   context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
   context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, host);


//   def downloadSite(requestParameters : ArrayBuffer[NameValuePair]) : String  =
//   {
//     val encodedString = URLEncodedUtils.format(requestParameters, HTTP.DEF_CONTENT_CHARSET.displayName())
//     return downloadSite("/search?" + encodedString)
//   }


   /**
    * Download site from CiteSeerX. This open new connection.
    * @param requestString String that will be attached to the citeseer adres e.g.
    *                      /viewdoc/summary;jsessionid=2C33AF829B75EDF34EAF51C3AA23B445?doi=10.1.1.155.2813
    * @return Content of the page, raw html.
    */
   def downloadSite(requestString : String) : String =
   {
     if (!conn.isOpen) {
       val socket: Socket = new Socket(host.getHostName, host.getPort)
       conn.bind(socket, params)
     }
     var request: BasicHttpEntityEnclosingRequest = new BasicHttpEntityEnclosingRequest("GET",requestString)
     request.setParams(params)
     httpexecutor.preProcess(request, httpproc, context)
     var response: HttpResponse = httpexecutor.execute(request, conn, context)
     response.setParams(params)
     httpexecutor.postProcess(response, httpproc, context)
     var responseEntity: HttpEntity = response.getEntity
     var responseString: String = EntityUtils.toString(responseEntity)
     EntityUtils.consume(responseEntity)

     if (!connStrategy.keepAlive(response, context)) {
       conn.close();
     }
     return responseString
   }

   def closeConnection()
   {
     if(conn.isOpen)
       conn.close();
   }
 }

/**
 * Hold constant values
 */
object CiteSeerInfoExtraction {

  final val LINK = "link"
  final val PUB_INFO = "pubinfo"
  final val SNIPPET = "snippet"
  final val PUB_EXTRAS="pubextras"
  final val PUB_TOOLS = "pubtools"
  final val HREF = "href"
  final val TEXT="text"
  final val YEAR = "year"
  final val AUTHORS = "authors"
  final val ABSTRACT = "abstract"
  final val PAGER = "pager"

  final val regexPattern = new scala.util.matching.Regex(
    """(?s)(?i)<div class=\"result\"[^>]*>(.+?)""" +
      """<div class=\"pubinfo\">(.+?)</div>.+?""" +
      """<div class=\"snippet\">(.+?)</div>.+?""" +
      """<div class=\"pubextras\">(.+?)</div>.+?""" +
      """<div class=\"pubtools\">(.+?)</div>""" +
      """.*?</div>""", LINK, PUB_INFO, SNIPPET, PUB_EXTRAS, PUB_TOOLS)

  final val linkRegex = new scala.util.matching.Regex(
    """(?s)(?i)<a class=\"remove doc_details"\ href=\"(.+?)\">(.+?)</a>""", HREF, TEXT)


  final val pubInfoAuthorRegex = new scala.util.matching.Regex(
    """(?s)(?i)<span class=\"authors\">(.+?)</span>""", AUTHORS
  )
  final val pubInfoYearRegex = new scala.util.matching.Regex(
    """(?s)(?i)<span class=\"pubyear\">.*?(\d{4}).*?</span>""", YEAR
  )


  final val abstractRegex = new scala.util.matching.Regex(
  """(?s)(?i)<div id=\"abstract\">.*?<p>(.+?)</p>.*?</div>""",ABSTRACT
  )


  final val pagerRegex = new scala.util.matching.Regex(
    """(?s)(?i)<div id=\"pager\">.*?<a href=\"(.*?)\">""",PAGER
  )  //<a href=\"(.*?)\>.*?</a></div>
}

class CiteSeerInfoExtraction(htmlSite:String)
{

  val extracts = ArrayBuffer[scala.collection.mutable.Map[String,String]]()
  var pager : Option[String] = None

  import CiteSeerInfoExtraction._
  def extract()
  {
    val it = regexPattern.findAllIn(htmlSite).matchData
    while (it.hasNext)
    {
      val data = scala.collection.mutable.Map[String,String]()
      val matcher = it.next()
      val linkText: String = matcher.group(LINK) //.replaceAll("\n","")
      val rawTitle = CiteSeerGate.removeHtmlTags(linkRegex.findFirstMatchIn(linkText).get.group(TEXT).trim())
      data+= (TEXT -> rawTitle)
      val hrefPart = linkRegex.findFirstMatchIn(linkText).get.group(HREF).trim()
      data+= (HREF->hrefPart)
      val pubInfoText: String = matcher.group(PUB_INFO)
      data += (PUB_INFO -> pubInfoText)
      val authors = pubInfoAuthorRegex.findFirstMatchIn(pubInfoText).
        map(_.group(AUTHORS).trim.stripPrefix("by").trim).getOrElse("Unknwon")
      data+= (AUTHORS->authors)
      val pubyear = pubInfoYearRegex.findFirstMatchIn(pubInfoText).map(_.group(YEAR)).getOrElse("Unknown")
      data+=(YEAR->pubyear)
      pager = pagerRegex.findFirstMatchIn(htmlSite).map(_.group(PAGER))
      extracts+=data
  }

}
}

class CiteSeerGate
{
  def getAbstracts(works: scala.collection.mutable.ArrayBuffer[scala.collection.mutable.Map[String, String]]) {
    works.par.foreach {
      e =>
        val downloader = new CiteSeerHtmlDownloader;
        val articleDetailsHtmlSite = downloader.downloadSite(e(CiteSeerInfoExtraction.HREF))
        val abstractText =
          CiteSeerInfoExtraction.abstractRegex.findFirstMatchIn(articleDetailsHtmlSite)
            .map(_.group(CiteSeerInfoExtraction.ABSTRACT)).getOrElse("Unknown")
        e += (CiteSeerInfoExtraction.ABSTRACT -> abstractText)
        downloader.closeConnection()
    }
  }

  def searchForPublications(publicationKeyword : String, limit:Int) : List[Map[String, String]] =
  {
    val requestParameters = ArrayBuffer[NameValuePair]()
    requestParameters+=(new BasicNameValuePair("q", publicationKeyword))
    requestParameters+=(new BasicNameValuePair("submit", "Search"))
    requestParameters+=(new BasicNameValuePair("sort", "rlv"))
    requestParameters+=(new BasicNameValuePair("t", "doc"))
    return processIt(requestParameters, limit)
  }

  def searchForAuthors(authorKeyword : String, limit:Int) : List[Map[String, String]] =
  {
    import scala.runtime.RichChar
    var author = if(authorKeyword.exists(_.isWhitespace)) "(%s)".format(authorKeyword) else authorKeyword
    val requestParameters = ArrayBuffer[NameValuePair]()
    requestParameters+=(new BasicNameValuePair("q", "author:"+author))
    requestParameters+=(new BasicNameValuePair("submit", "Search"))
    requestParameters+=(new BasicNameValuePair("sort", "rlv"))
    requestParameters+=(new BasicNameValuePair("t", "doc"))
    return processIt(requestParameters, limit)
  }

  def processIt(requestParameters: ArrayBuffer[NameValuePair], limit: Int): scala.List[Map[String, String]] = {
    val encodedString = URLEncodedUtils.format(requestParameters, HTTP.DEF_CONTENT_CHARSET.displayName())
    val fullUrl = "/search?" + encodedString
    println(fullUrl)
    var nextPagina: Option[String] = Some[String](fullUrl)
    var downloader = new CiteSeerHtmlDownloader;
    var buffer = ArrayBuffer[scala.collection.mutable.Map[String, String]]()
    var results = 0
    while (nextPagina != None && results <= limit) {
      val siteContent = downloader.downloadSite(nextPagina.get)
      val extractor = new CiteSeerInfoExtraction(siteContent)
      extractor.extract()
      nextPagina = extractor.pager
      results += extractor.extracts.length
      //println(extractor.extracts.length)
      buffer ++= extractor.extracts
    }
    val ret: List[Map[String, String]] = buffer.toList
    return ret
  }
}

object CiteSeerGate
{
  def removeHtmlTags(str: String): String = {
    return str.replaceAll("""(<(\w+)(.*?)>)|(</\w+>)""", "")
  }

  def main(args: Array[String])
  {
    val csg = new CiteSeerGate
    val i = csg.searchForAuthors("Henryk Rybinski",30)
    for (p <- i)
    {
      println(p.getOrElse(CiteSeerInfoExtraction.AUTHORS,"Missing authors"))
    }
  }
}