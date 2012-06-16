package pl.edu.pw.elka.stud.tkogut.sade.core.yellowpages

import pl.edu.pw.elka.stud.tkogut.sade.core._
import pl.edu.pw.elka.stud.tkogut.sade.messages._

/**
 * Trait causes Agent to register in YellowPages book.
 */
trait YellowPagesSearchable extends Agent {
  YellowPagesAgent ! RegisterAgent(this, super.name)
}