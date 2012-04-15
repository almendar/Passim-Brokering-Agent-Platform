package pl.edu.pw.elka.stud.tkogut.passim.search.dummy
import pl.edu.pw.elka.stud.tkogut.brokering.messages._
import pl.edu.pw.elka.stud.tkogut.sade.core._
import pl.edu.pw.elka.stud.tkogut.sade.messages._
import pl.edu.pw.elka.stud.tkogut.passim._
import java.io.FileWriter
import pl.edu.pw.elka.stud.tkogut.brokering.BrokerAgent

class DummyClient(name: String, BA:BrokerAgent) extends Agent(name) {
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