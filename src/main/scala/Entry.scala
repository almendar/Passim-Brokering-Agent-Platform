import scala.Console
import pl.edu.pw.elka.stud.tkogut.passim.agents.searchers.BingSearcherAgent
import pl.edu.pw.elka.stud.tkogut.passim.agents.brokering.BrokerAgent
import pl.edu.pw.elka.stud.tkogut.passim.agents.yellowpages.YellowPagesAgent
import pl.edu.pw.elka.stud.tkogut.passim.messages.QueryWeb
import pl.edu.pw.elka.stud.tkogut.passim.search.GoogleSearch
import net.liftweb.json.JsonAST._
import scala.util.parsing.json._
import java.io.BufferedReader
import java.io.InputStreamReader
import scala.util.parsing.json.Parser
import java.io.FileWriter
import java.io.File
import java.io.PrintWriter

object Entry {
  def main(args: Array[String]) {
    //testEstablishingDialog
    //testGoogleSearch
    //testFromInput
	println("Hello Agent Platform!")
  }

  private def testEstablishingDialog: Unit = {
    val broker = new BrokerAgent("Broker")
    val bingSearcher = new BingSearcherAgent("BingHelper")
    YellowPagesAgent.start
    bingSearcher.start
    broker.start
    Thread.sleep(500)
    broker ! QueryWeb("SOPA", null)

  }

  private def testFromInput: Unit = {
    val broker = new BrokerAgent("Broker")
    val bingSearcher = new BingSearcherAgent("BingHelper")
    val participanAgents = List(broker, bingSearcher, YellowPagesAgent)
    participanAgents.foreach(_.start())
    while (true) {
      val s = Console.readLine()
      broker ! QueryWeb(s, null)
    }
  }

  private def testGoogleSearch: Unit = {

    // val json = JSON.parse(jsonDocString)
    //
    //val a = for { JField("snippet", JString(value)) <- json } yield value
    //println(a)
    /*val out = new PrintWriter(new File("out.json"))
    try { out.println(jsonDocString) }
    finally { out.close }
    */

    /*
    var restOfList: List[Any] = json.get.tail
    var head: Any = json.get.head

    var queries: List[Any] = null
    var url: List[Any] = null
    var context: List[Any] = null
    var items: List[Any] = null

    for (i <- 1 to json.get.length - 1) {
      var headTyped = head.asInstanceOf[Tuple2[String, List[Any]]]
      println(headTyped._1)
      headTyped._1 match {
        case "queries" => queries = headTyped._2
        case "url"     => url = headTyped._2
        case "context" => context = headTyped._2
        case "items"   => items = headTyped._2
      }
      head = restOfList.head
      restOfList = restOfList.tail
    }

    var elementsAll: List[Any] = List(queries, url, context, items)
    elementsAll.foreach(println(_))
    */

  }
}