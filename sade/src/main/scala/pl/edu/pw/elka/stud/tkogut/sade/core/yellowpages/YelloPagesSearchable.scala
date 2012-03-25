package pl.edu.pw.elka.stud.tkogut.sade.core.yellowpages

import pl.edu.pw.elka.stud.tkogut.sade.core._
import pl.edu.pw.elka.stud.tkogut.sade.messages._

trait YelloPagesSearchable extends Agent {
  YellowPagesAgent ! RegisterAgent(this, super.name)
}