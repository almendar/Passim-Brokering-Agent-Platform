package pl.edu.pw.elka.stud.tkogut.passim.sade.core.yellowpages
import pl.edu.pw.elka.stud.tkogut.passim.sade.core.yellowpages._
import pl.edu.pw.elka.stud.tkogut.passim.sade.core._
import pl.edu.pw.elka.stud.tkogut.passim.sade.messages._

trait YelloPagesSearchable extends Agent {
  YellowPagesAgent ! RegisterAgent(this, super.name)
}