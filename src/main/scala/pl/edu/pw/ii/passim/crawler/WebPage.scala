package pl.edu.pw.ii.passim.text

import io.Source
import java.net.URI
import java.util.regex.Matcher
import pl.edu.pw.ii.passim.crawler.DownloadAgent

/**
 * Author: Piotr Kołaczkowski
 * Date: 2010-08-05
 * Time: 14:14:38
 */

/**
 * Represents a full document.
 * Provides way to extract body, links and paragraphs from it.
 */
class WebPage(val uri: URI, val content: String) {

  override def toString = "WebPage(" + uri.toString + ")"

  override def hashCode = uri.hashCode

  override def equals(other: Any) = other.isInstanceOf[WebPage] && other.asInstanceOf[WebPage].uri == uri

  /**
   * Returns the body part of the document, without scripts and with all tags in lowercase.
   */
  lazy val body: String = {

    def lowerTags(s: String): String =
      "<[^>]*>".r.replaceAllIn(s, x => Matcher.quoteReplacement(x.toString.toLowerCase))

    def removeScripts(s: String, sb: StringBuilder = new StringBuilder): String = {
      val scriptStartPattern = "<script[^>]*>".r
      val scriptEndPattern = "</script>".r
      val start = scriptStartPattern.findFirstMatchIn(s)
      val end = scriptEndPattern.findFirstMatchIn(s)
      if (start.isDefined && end.isDefined) {
        sb.append(s.substring(0, start.get.start))
        removeScripts(s.substring(end.get.end), sb)
      } else if (start.isDefined) {
        sb.append(s.substring(0, start.get.start))
        sb.toString
      } else {
        sb.append(s)
        sb.toString
      }
    }

    val lowerContent = lowerTags(content)
    val noScriptContent = removeScripts(lowerContent)
    val bodyStartPattern = "<body[^>]*>".r
    val bodyEndPattern = "</body>".r
    val match1 = bodyStartPattern.findFirstMatchIn(noScriptContent)
    val match2 = bodyEndPattern.findFirstMatchIn(noScriptContent)
    if (match1.isDefined && match2.isDefined)
      noScriptContent.substring(match1.get.end, match2.get.start)
    else if (match1.isDefined)
      noScriptContent.substring(match1.get.end)
    else
      ""
  }


  /**
   * Returns list of navigable links contained in the document.
   * Links with commonly used extensions pointing to binary content are not returned.
   */
  lazy val links: List[URI] = {
    val pattern = "<a[^>]+href=\"([^\"]+)\"[^>]*>".r
    pattern.findAllIn(body)
           .map(x => pattern.findFirstMatchIn(x).get.group(1))
           .filterNot(_.contains("#"))
           .map(x => try { Some(uri.resolve(x)) } catch { case _: Exception => None } )
           .filter(_.isDefined)
           .map(_.get)
           .filterNot(_.getPath != null)
           .toList.distinct
  }

  /**
   * Text with no tags, no formatting, with
   * excessive whitespace trimmed.
   */
  lazy val text = WebPage.cleanText(body)
}


object WebPage {

  lazy val stopWords: Array[String] = {
    val is = this.getClass.getResourceAsStream("/stopwords-pl.dat")
    Source.fromInputStream(is, "utf-8").mkString.split(", *")
  }


  lazy val entities = Map(
    "&quot;" -> "\"",
    "&amp;" -> "&",
    "&lt;" -> "<",
    "&gt;" -> ">",
    "&raquo;" -> ">>",
    "&laquo;" -> "<<",
    "&nbsp;" -> " ",
    "&copy;" -> "©",
    "&oacute;" -> "ó")

  def cleanText(content: String) = {
    val c2 = "&[a-z]+;".r.replaceAllIn(content,
      x => entities.getOrElse(x.toString, Matcher.quoteReplacement(x.toString)))
    val c3 = "&#([0-9]+);".r.replaceAllIn(c2,
      x => Matcher.quoteReplacement(Integer.parseInt(x.group(1)).asInstanceOf[Char].toString))

    c3.replaceAll("\\s+", " ")
      .replaceAll("<[^>]*>", " ")
      .replaceAll("<br/>|<br>|</tr>", "\n")
      .replaceAll(" +", " ")
      .replaceAll("( *\n+ *)+", "\n")
      .trim
  }

  def fromUri(uri: URI): WebPage =
    new DownloadAgent download(uri)


  def fromUri(uri: URI, timeout: Int): WebPage =
    new DownloadAgent download(uri, timeout)

}

