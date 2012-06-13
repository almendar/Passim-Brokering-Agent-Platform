package pl.edu.pw.elka.stud.tkogut.brokering

import collection.mutable.ListBuffer
import pl.edu.pw.elka.stud.tkogut.brokering.tools.SingleSearchResult

/**
 * Created with IntelliJ IDEA.
 * User: tomek
 * Date: 11.06.12
 * Time: 18:18
 * To change this template use File | Settings | File Templates.
 */

class SearchTask(taskId: String) extends Task(taskId) {
  var nrOfAnswersLeft = 0;
  val answers: ListBuffer[List[SingleSearchResult]] = new ListBuffer[List[SingleSearchResult]]()
  var nextFree: Int = 0;
  var query: String = null
}