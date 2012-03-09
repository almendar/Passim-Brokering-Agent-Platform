/**
 *
 */
package pl.edu.pw.elka.stud.tkogut.passim.brokering.tools
import java.net.URI
import scala.collection.mutable.Map
/**
 * @author Tomek
 *
 */
class SearchResultMerge {
  private var links = Map[URI, SingleSearchResult]()

  def addSearchResutl(result: SingleSearchResult) {
    val resultURI = result.mURL.toURI()

    if (links.contains(resultURI)) {
      val alreadyFoundResult = links(resultURI)
      val description = if (alreadyFoundResult.mDescription != SingleSearchResult.MISSING_DESCRIPTION) alreadyFoundResult.mDescription else result.mDescription
      val title = if (alreadyFoundResult.mTitle != SingleSearchResult.MISSING_TITLE) alreadyFoundResult.mTitle else result.mDescription
      val additionlAttribs = Map[String, String]()
      additionlAttribs ++= result.mAdditionalAttributes
      additionlAttribs ++= alreadyFoundResult.mAdditionalAttributes
      val mergedResult = new SingleSearchResult(resultURI.toURL(), title, description)
      mergedResult.mAdditionalAttributes ++= additionlAttribs
      links(resultURI) = mergedResult
    } else {
      links += (resultURI -> result)
    }
  }

  def addResults(listOfResults: List[SingleSearchResult]) {
    listOfResults.foreach(addSearchResutl(_))
  }

  def getResultList = links.values.toList

}