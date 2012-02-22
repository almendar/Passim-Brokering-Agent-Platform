package pl.edu.pw.elka.stud.tkogut.passim.agents
import pl.edu.pw.elka.stud.tkogut.passim.agents.yellowpages.YellowPagesAgent
import pl.edu.pw.elka.stud.tkogut.passim.messages.RegisterKnowledgeSource

trait YelloPagesSearchable {

  registerInYelloPages()

  def registerInYelloPages() {
    YellowPagesAgent ! RegisterKnowledgeSource(this, uniqueName())
  }

  def uniqueName(): String;
}