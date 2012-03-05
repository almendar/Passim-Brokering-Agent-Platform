package pl.edu.pw.elka.stud.tkogut.passim.messages

import pl.edu.pw.elka.stud.tkogut.passim.agents.searchers.SearchAgent
import pl.edu.pw.elka.stud.tkogut.passim.agents.Agent

case class WebSearchSourceList(list: List[SearchAgent]) extends Message