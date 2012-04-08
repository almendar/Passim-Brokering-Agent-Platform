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
    val talker = new Agent("Controller") {
      override def handleMessage(msg: Message) {
        msg match {
          case x: SearchResultMessage =>
            val fw = new FileWriter("result.txt")
            fw.write(x.toString)
            fw.close()
          case AskForSearch(y) =>
            var dialaogID: String = null
            dialaogID = establishDialog(BA,
              (id: String) => {
                BA ! QueryMessage(y, id)
              })
        }
      }
      override def processDialog(id: String) {
        activeDialogs(id).nextAction(id)
      }
    }

    BA.start
    talker.start

        while (true) {
          val q = readLine()
          talker ! AskForSearch(q)
    
        }

    talker ! AskForSearch("Java")

  }

}