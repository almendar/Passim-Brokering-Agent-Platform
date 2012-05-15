package pl.edu.pw.ii.passim.crawler

import java.io.{ByteArrayOutputStream, InputStream}
import java.net.URI
import actors.Actor
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.commons.logging.LogFactory
import org.apache.http.client.methods.HttpGet
import org.apache.http.params.HttpConnectionParams
import org.apache.http.util.EntityUtils
import io.Source
import pl.edu.pw.ii.passim.text.WebPage

/**
 * Author: Piotr Kołaczkowski
 * Date: 12.05.11
 * Time: 12:56
 */

case class DownloadCompleted(document: WebPage, agent: DownloadAgent)
case class DownloadFailed(exception: Exception, uri: URI, agent: DownloadAgent)
case class RequestDownload(uri: URI)

class DownloadAgent(cache: URI => Option[WebPage] = (_ => None)) extends Actor {

  lazy val client = new DefaultHttpClient

  def download(uri: URI,
               timeout: Int = Int.MaxValue,
               acceptedContentType: String = "text/.*",
               headers: Iterable[(String, String)] = Nil): WebPage = {
    val logger = LogFactory.getLog(getClass)
    val startTime = System.currentTimeMillis
    logger.info("Downloading: " + uri)

    val method = new HttpGet(uri)
    val params = method.getParams
    HttpConnectionParams.setConnectionTimeout(params, timeout)
    HttpConnectionParams.setSoTimeout(params, timeout)

    method.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.0; rv:2.0.1) Gecko/20100101 Firefox/4.0.1")
    method.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    method.addHeader("Accept-Language", "pl,en-us;q=0.7,en;q=0.3")
    method.addHeader("Accept-Charset", "ISO-8859-2,utf-8;q=0.7,*;q=0.7")
    method.addHeader("Referer", "http://www.ii.pw.edu.pl/")
    headers.foreach { case (h, v) => method.addHeader(h, v) }

    // Send the request here:
    val response = client.execute(method)
    logger.info(response.getStatusLine.toString)

    val entity = response.getEntity
    val contentType = entity.getContentType.getValue
    if (!contentType.matches(acceptedContentType))
      new WebPage(uri, "")
    else {
      def byteArray(is: InputStream): Array[Byte] = {
        val bs = new ByteArrayOutputStream
        val buffer = new Array[Byte](1024)
        var count = is.read(buffer);
        while (count != -1 && System.currentTimeMillis - startTime < timeout) {
          bs.write(buffer, 0, count)
          count = is.read(buffer)
        }
        is.close()
        bs.close()
        bs.toByteArray
      }

      val binaryContent = byteArray(entity.getContent)
      val asciiContent = Source.fromBytes(binaryContent, "iso-8859-1").mkString.toLowerCase
      val contentTypePattern = "<meta[^>].*http-equiv *= *\"content-type\" *content=\"[a-z0-9.-/]*; *charset *= *([a-z0-9-]*)\"".r
      val contentTypeMatch = contentTypePattern.findFirstMatchIn(asciiContent)

      val charsetEncoding =
        if (contentTypeMatch.isDefined)
          contentTypeMatch.get.group(1)
        else
          EntityUtils.getContentCharSet(entity)
      val content =
        if (charsetEncoding != null)
          Source.fromBytes(binaryContent, charsetEncoding).mkString
        else
          Source.fromBytes(binaryContent, "iso-8859-2").mkString
      logger.info("Finished downloading: " + uri)
      new WebPage(uri, content)
    }
  }

  def act() {
    loop {
      react {
        case RequestDownload(uri) =>
          try {
            val document = cache(uri).getOrElse(WebPage fromUri uri)
            reply(DownloadCompleted(document, this))
          } catch {
            case e: Exception => reply(DownloadFailed(e, uri, this))
          }
      }
    }
  }

}