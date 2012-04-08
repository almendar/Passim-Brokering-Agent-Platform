package pl.edu.pw.elka.stud.tkogut.passim.tools
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import java.net.URL
import java.util.Calendar
import java.util.Date
import pl.edu.pw.elka.stud.tkogut.brokering.tools.SearchResultMerge
import pl.edu.pw.elka.stud.tkogut.brokering.tools.SingleWebSearchResult


class HttpAdressMergerTest extends FunSuite {  
  test("Merge same host") {
    val srm = new SearchResultMerge
    val s1 = new SingleWebSearchResult(new URL("http://google.pl"), "google", "Search engine google")
    val s2 = new SingleWebSearchResult(new URL("http://www.google.pl/search?q=to+held&ie=utf-8&oe=utf-8&aq=t&rls=org.mozilla:pl:official&client=firefox-a#sclient=psy-ab&hl=pl&client=firefox-a&hs=mqM&rls=org.mozilla:pl%3Aofficial&source=hp&q=wiatr&pbx=1&oq=wiatr&aq=f&aqi=g4&aql=&gs_sm=e&gs_upl=708440l709165l0l709240l5l5l0l0l0l0l453l1058l2-1.1.1l3l0&bav=on.2,or.r_gc.r_pw.r_cp.,cf.osb&fp=63ccd5c2fd46a7c0&biw=1378&bih=811"), "wiatr - Szukaj w google", "Looking for wind in google")
    srm.addSearchResutl(s1)
    srm.addSearchResutl(s2)
    val resultList = srm.getResultList
    assert(resultList.length == 2)
  }

  test("Merge diffrent hosts") {
    val srm = new SearchResultMerge
    val s1 = new SingleWebSearchResult("http://google.pl", "google", "Search engine google")
    val s2 = new SingleWebSearchResult("http://bing.com", "bing", "Search enginee bing")
    srm.addSearchResutl(s1)
    srm.addSearchResutl(s2)
    val resultList = srm.getResultList
    assert(resultList.length == 2)
  }

  test("Same result with additional attribute") {
    val attrib = "DateTime"
    val srm = new SearchResultMerge
    val s1 = new SingleWebSearchResult("http://google.pl", "google", "Search engine google")
    s1.additionalAttributes += (attrib -> Calendar.getInstance().set(2012, 1, 1).toString())
    val s2 = new SingleWebSearchResult("http://google.pl", "googl1", "Search enginee google")
    srm.addSearchResutl(s1)
    srm.addSearchResutl(s2)
    val resultList = srm.getResultList
    assert(resultList(0).additionalAttributes(attrib).equals(Calendar.getInstance().set(2012, 1, 1).toString()))
    assert(resultList.length == 1)

  }
}