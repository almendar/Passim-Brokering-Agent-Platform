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
import collection.mutable.ArrayBuffer
import xml.{Node, XML}
import scala.util.matching.Regex.{MatchIterator, Match}
import scala.collection.JavaConversions._


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



   def downloadSite(requestParameters : ArrayBuffer[NameValuePair]) : String  =
   {
     //val requestEntity: HttpEntity = new UrlEncodedFormEntity(requestParameters, HTTP.DEF_CONTENT_CHARSET)
//     val encodedString = URLEncodedUtils.format(requestParameters, HTTP.DEF_CONTENT_CHARSET)
     val encodedString = URLEncodedUtils.format(requestParameters, HTTP.DEF_CONTENT_CHARSET.displayName())
     return downloadSite("/search?" + encodedString)
   }

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

   def closeConnection() {
     if(conn.isOpen)
       conn.close();
   }







 }

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
}

class CiteSeerInfoExtraction(htmlSite:String)
{

  val extracts = ArrayBuffer[scala.collection.mutable.Map[String,String]]()

  import CiteSeerInfoExtraction._
  def extract()
  {



    val it = regexPattern.findAllIn(htmlSite).matchData
    while (it.hasNext) {
      val data = scala.collection.mutable.Map[String,String]()

      val matcher = it.next()
      val linkText: String = matcher.group(LINK) //.replaceAll("\n","")
      val rawTitle = CiteSeerApp.removeHtmlTags(linkRegex.findFirstMatchIn(linkText).get.group(TEXT).trim())
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
      extracts+=data
  }

}
}

class CiteSeerGate
{
}

object CiteSeerApp
{
 def removeHtmlTags(str: String): String = {
   return str.replaceAll("""(<(\w+)(.*?)>)|(</\w+>)""", "")
  }


  def main(args: Array[String])
  {
    val nameValuePair = ArrayBuffer[NameValuePair]()
    nameValuePair+=(new BasicNameValuePair("q", "Datamining"))
    nameValuePair+=(new BasicNameValuePair("submit", "Search"))
    nameValuePair+=(new BasicNameValuePair("sort", "rlv"))
    nameValuePair+=(new BasicNameValuePair("t", "doc"))
    val downloader = new CiteSeerHtmlDownloader;

    val htmlDoc: String = downloader.downloadSite(nameValuePair)
    val  extrator = new CiteSeerInfoExtraction(htmlDoc);
    extrator.extract()


    extrator.extracts.par.foreach { e=>
      val downloader = new CiteSeerHtmlDownloader;
      val articleDetailsHtmlSite = downloader.downloadSite( e( CiteSeerInfoExtraction.HREF ) )
      val abstractText =
        CiteSeerInfoExtraction.abstractRegex.findFirstMatchIn( articleDetailsHtmlSite )
          .map(_.group(CiteSeerInfoExtraction.ABSTRACT)).getOrElse("Unknown")
      e+=(CiteSeerInfoExtraction.ABSTRACT -> abstractText)
      downloader.closeConnection()
    }
    println(extrator.extracts(0)(CiteSeerInfoExtraction.ABSTRACT))

    /*

    for(e <- extrator.extracts){
    val articleDetailsHtmlSite = downloader.downloadSite( e( CiteSeerInfoExtraction.HREF ) )
   // println(articleDetailsHtmlSite)
    println(CiteSeerInfoExtraction.abstractRegex.findFirstMatchIn( articleDetailsHtmlSite ).map(_.group(CiteSeerInfoExtraction.ABSTRACT)).getOrElse("Unknown"))
    }
    */







        //println(htmlDoc)
        println("==============");
        //println(htmlDoc)
        //template """(?s)(?i)<div([^>]+)>(.+?)</div>"""
        //processHtml(htmlDoc)

        //        val iterator: Iterator[Match] = regexPattern.findAllIn(htmlDoc).matchData
        //        println("Found results " + iterator.length)
        //        while(iterator.hasNext)
        //        {
        //
        //          val a: Match = iterator.next
        //          val linkText: String = a.group("link")
        //          println("&&&&&\n"+linkText+"&&&&\n\n")
        //          val linkRegex = new scala.util.matching.Regex(
        //            """"(?s)(?i)<a class=\"remove doc_details\" href=\"(.+?)\"[^>]*>(.+?)</a>""","href","text")
        //          val desc = linkRegex.findFirstMatchIn(linkText)
        //          if(desc!=None)
        //            println(desc.get.group("text") )
        //          else
        //            println(None)
        //        }

        //println("First match: " + text.get.group("link"))
        //regexPattern.




    //makeGET(conn, host, params, httpexecutor, httpproc, context, connStrategy)
  }

  def makeGET(conn: DefaultHttpClientConnection, host: HttpHost, params: HttpParams, httpexecutor: HttpRequestExecutor, httpproc: HttpProcessor, context: BasicHttpContext, connStrategy: DefaultConnectionReuseStrategy) {
    try {

      val targets = Array[String](
        "/search"
      );

      for (tar <- targets) {
        if (!conn.isOpen()) {
          val socket = new Socket(host.getHostName(), host.getPort());
          conn.bind(socket, params);
        }
        val request = new BasicHttpRequest("GET", tar);
        println(">> Request URI: " + request.getRequestLine().getUri());

        request.setParams(params);
        httpexecutor.preProcess(request, httpproc, context);
        val response: HttpResponse = httpexecutor.execute(request, conn, context);
        response.setParams(params);
        httpexecutor.postProcess(response, httpproc, context);
        //      println(response.getHeaders("Location").mkString(","))
        System.out.println("<< Response: " + response.getStatusLine());
        println(response.getStatusLine.getReasonPhrase)
        System.out.println(EntityUtils.toString(response.getEntity()));
        System.out.println("==============");
        if (!connStrategy.keepAlive(response, context)) {
          conn.close();
        } else {
          System.out.println("Connection kept alive...");
        }
      }
    } finally {
      conn.close();
    }
  }
}


