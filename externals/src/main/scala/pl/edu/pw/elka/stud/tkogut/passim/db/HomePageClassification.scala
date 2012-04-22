package pl.edu.pw.elka.stud.tkogut.passim.db

import pl.edu.pw.elka.stud.tkogut.passim.search.bing.BingSearch
import pl.edu.pw.ii.passim.homepages.workflow.ClassifyTask22


object HomePageClassification {

  def main(args: Array[String]) {
    val b = new BingSearch("8A4C8362BAF8F435BCF3F8854CBEF493006E398A");
    val lookUpQuery: String = "Shiguang Shan"
    val r = b.search(lookUpQuery)
    var i = 0
    val cl = new ClassifyTask22(lookUpQuery)
    val homePageClassificationResult = r.map {
      x =>
        cl.block(x.mURL.toString)
    }.toList
    homePageClassificationResult.filter(_._2 > 1.0).sortWith(_._2 > _._2).foreach(y => println {
      y._1.toString + ":" + y._2 + "\n"
    })
  }

}
