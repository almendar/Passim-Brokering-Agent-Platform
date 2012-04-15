package pl.edu.pw.elka.stud.tkogut.passim.db

import pl.edu.pw.elka.stud.tkogut.passim.search.bing.BingSearch
import pl.edu.pw.ii.passim.text.WebPage
import java.io.FileWriter

object EntryPointBattle {

  def main(args: Array[String]) {
    val b = new BingSearch("8A4C8362BAF8F435BCF3F8854CBEF493006E398A");
    val r = b.search("datamining")
    val siteContents =r.map{ bsr  =>
      (bsr.mURL.toURI, WebPage fromUri (bsr.mURL.toURI))

    }
    var i=0
    for (x <- siteContents) {
      i+=1
      val fw:FileWriter = new FileWriter(i.toString + ".txt")
      fw.write(x._1.toString + "\n\n")
      fw.write(x._2.text)
      fw.close
    }
  }

}
