/**
 *
 */
package pl.edu.pw.elka.stud.tkogut.brokering.tools
import java.net.URI
import scala.collection.mutable.Map
import scala.collection.mutable.ListBuffer
/**
 * @author Tomek
 *
 */
class SearchResultMerge {
  private var links = Map[URI, SingleSearchResult]()
  private var noUri = new ListBuffer[SingleSearchResult]()

  def addSearchResutl(result: SingleSearchResult) {

    result match {
      case webRes: SingleWebSearchResult => {
        val resultURI = webRes.mURL.toURI()
        if (links.contains(resultURI)) {
          val alreadyFoundResult = links(resultURI)
          val description = if (alreadyFoundResult.mDescription != SingleSearchResult.MISSING_DESCRIPTION) alreadyFoundResult.mDescription else result.mDescription
          val title = if (alreadyFoundResult.mTitle != SingleSearchResult.MISSING_TITLE) alreadyFoundResult.mTitle else result.mDescription
          val additionlAttribs = Map[String, String]()
          additionlAttribs ++= result.additionalAttributes
          additionlAttribs ++= alreadyFoundResult.additionalAttributes
          val mergedResult = new SingleWebSearchResult(resultURI.toURL(), title, description)
          mergedResult.additionalAttributes ++= additionlAttribs
          links(resultURI) = mergedResult
        } else {
          links += (resultURI -> result)
        }

      }
      case loclRes: SingleSearchResult => noUri.append(loclRes)
    }

  }

  def addResults(listOfResults: List[SingleSearchResult]) {
    listOfResults.foreach(addSearchResutl(_))
  }

  def getResultList = (links.values.toList ::: noUri.toList ::: Nil)

}