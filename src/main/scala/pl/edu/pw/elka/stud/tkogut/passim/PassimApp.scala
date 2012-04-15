package pl.edu.pw.elka.stud.tkogut.passim

import pl.edu.pw.elka.stud.tkogut.passim.search.google.GoogleSearcherAgent
import pl.edu.pw.elka.stud.tkogut.passim.search.dummy.DummyClient
import pl.edu.pw.elka.stud.tkogut.passim.search.bing.BingSearcherAgent
import pl.edu.pw.elka.stud.tkogut.passim.search.dblp.MongoDblpSearchAgent
import pl.edu.pw.elka.stud.tkogut.brokering.BrokerAgent

import pl.edu.pw.elka.stud.tkogut.sade.messages.Message
import pl.edu.pw.elka.stud.tkogut.sade.core.yellowpages.YellowPagesAgent
import collection.immutable.List

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


    BA.start
    val client1 = new DummyClient("Client1", BA)
    val client2 = new DummyClient("Client2", BA)

    client1.start
    client2.start

    val queries1: List[String] = List(
      "Brokering",
      "Automotive",
      "OpenCL",
      "Datamining",
      "Spatial databases",
      "GIS systems")

    val queries2: List[String] = List(
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