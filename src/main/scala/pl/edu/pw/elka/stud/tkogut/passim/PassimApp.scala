package pl.edu.pw.elka.stud.tkogut.passim
import java.io.File
import pl.edu.pw.elka.stud.tkogut.brokering.dialect._
import pl.edu.pw.elka.stud.tkogut.passim.search.google.GoogleSearcherAgent
import pl.edu.pw.elka.stud.tkogut.passim.search.bing.BingSearcherAgent
import pl.edu.pw.elka.stud.tkogut.passim.search.dblp.MongoDblpSearchAgent
import pl.edu.pw.elka.stud.tkogut.brokering.BrokerAgent
import pl.edu.pw.elka.stud.tkogut.sade.core.Agent
import pl.edu.pw.elka.stud.tkogut.sade.messages.Message
import pl.edu.pw.elka.stud.tkogut.brokering.messages.SearchResultMessage
import pl.edu.pw.elka.stud.tkogut.brokering.messages.QueryMessage
import pl.edu.pw.elka.stud.tkogut.sade.core.yellowpages.YellowPagesAgent
import java.io.FileWriter

case class AskForSearch(q: String) extends Message

object PassimApp {

  def main(args: Array[String]): Unit = {
    YellowPagesAgent.start()
    val searchAgentsList = List(
      new GoogleSearcherAgent("GSA"),
      new BingSearcherAgent("BSA"),
      new MongoDblpSearchAgent("MDSA"))
    searchAgentsList.foreach(_.start)

    Thread.sleep(300)
    val BA = new BrokerAgent("Broker")

    class Talker(name: String) extends Agent(name) {
      override def handleMessage(msg: Message) {
        msg match {
          case x: SearchResultMessage =>
            val query = activeDialogs(x.dialogID).attributes("Query").toString
            val outFileName = "query_" + name + "_" + query.replace(" ", "_") + ".txt"
            speak("Got result for query:" + query)
            val fw = new FileWriter(outFileName)

            fw.write(x.toString)
            fw.close()
          case AskForSearch(y) =>
            var dialaogID: String = null
            dialaogID = establishDialog(BA,
              (id: String) => {
                BA ! QueryMessage(y, id)
              })
            activeDialogs(dialaogID).attributes += ("Query" -> y)
        }
      }
      override def processDialog(id: String) {
        activeDialogs(id).nextAction(id)
      }
    }

    BA.start
    val client1 = new Talker("Client1")
    val client2 = new Talker("Client2")

    client1.start
    client2.start

    val queries1 = List(
      "Brokering",
      "Automotive",
      "OpenCL",
      "Datamining",
      "Spatial databases",
      "GIS systems")

    val queries2 = List(
      "Object oriented programming",
      "Multiagent systems",
      "Java",
      "OpenCL",
      "Zupa pomidorowa")

    queries1.foreach(client1 ! AskForSearch(_))
    queries2.foreach(client2 ! AskForSearch(_))

    /* while (true) {
      val q = readLine()
      talker ! AskForSearch(q)

    }
    */

    //talker ! AskForSearch("Java")

  }

}