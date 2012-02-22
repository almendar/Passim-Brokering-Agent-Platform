package pl.edu.pw.elka.stud.tkogut.passim.agents.brokering

import scala.actors.Actor._
import scala.Console
import scala.collection.mutable.Map
import pl.edu.pw.elka.stud.tkogut.passim.agents.searchers.BingSearcherAgent
import pl.edu.pw.elka.stud.tkogut.passim.search.BingSearchSingleResult
import pl.edu.pw.elka.stud.tkogut.passim.agents._
import pl.edu.pw.elka.stud.tkogut.passim.agents.yellowpages.YellowPagesAgent
import pl.edu.pw.elka.stud.tkogut.passim.messages._
import pl.edu.pw.elka.stud.tkogut.passim.search.SingleSearchResult
import pl.edu.pw.elka.stud.tkogut.passim.agents.searchers.SearchAgent
import pl.edu.pw.elka.stud.tkogut.passim.messages.SendWebSearchSourceRequest
import pl.edu.pw.elka.stud.tkogut.passim.tools.SearchResultMerge

class BrokerAgent(nameOfAgent: String) extends Agent(nameOfAgent) {

  private var mSearchAgentsList: List[SearchAgent] = null;

  private val mSearchAgentDialogIdToSearchTaskId = Map[String, String]()
  private val mSearchTaskIdToQueryAgentDialogId = Map[String, String]()

  /**
   * Maps dilogID with SearchAgents to associated task
   */
  private val mTasksMap = Map[String, SearchTask]()

  getSearchSources()

  override def handleMessage(msg: Message) {
    msg match {
      case res: SearchResultMessage => processSearchResult(res)
      case lst: WebSearchSourceList => saveSearchResources(lst)
      case query: QueryWeb          => addSearchTask(query) //establishDialog(searchAgentsList.head) //a.head ! Query(query.q, this) //() => { searchAgentsList.head ! Query(query.q, this) }
    }
  }

  override def processDialog(id: String) = {
    val dialog: Dialog = activeDialogs(id)
    val searchAgent = dialog.mContact
    val taskId = mSearchAgentDialogIdToSearchTaskId(id)
    val searchTask = mTasksMap(taskId)
    val queryText: String = searchTask.query
    searchAgent ! QueryWeb(queryText, id)
  }

  private def getSearchSources() = {
    YellowPagesAgent ! SendWebSearchSourceRequest(this)
  }

  private def processSearchResult(res: SearchResultMessage) = {

    val st: SearchTask = mTasksMap(mSearchAgentDialogIdToSearchTaskId(res.DialogID))
    st.nrOfAnswersLeft -= 1;
    st.answers(st.nextFree) = res.results
    if (st.nrOfAnswersLeft > 0) {
      st.nextFree += 1
    } else {
      speak("Got all answers for query:" + st.query)
      val merger = new SearchResultMerge()
      st.answers.foreach(merger.addResults(_))
      val askerDialogIdm = mSearchTaskIdToQueryAgentDialogId(st.taskID)
      val answerToQuery = new SearchResultMessage(askerDialogIdm)
      answerToQuery.results = merger.getResultList
      activeDialogs(askerDialogIdm).mContact ! answerToQuery
    }
  }

  private def saveSearchResources(lst: WebSearchSourceList): Unit = {
    speak("Received list with search agents")
    if (lst.list.length != 0) mSearchAgentsList = lst.list
    else getSearchSources()
  }

  private def addSearchTask(query: QueryWeb) {
    val searchAgentsNumber = mSearchAgentsList.length
    val askerDialogID = query.dialogId
    val taskID = generateID()
    mSearchTaskIdToQueryAgentDialogId += (taskID -> askerDialogID)
    val task = new SearchTask(taskID)
    task.nrOfAnswersLeft = searchAgentsNumber
    task.query = query.q
    mTasksMap(taskID) = task
    mSearchAgentsList.foreach { x: Agent =>
      val searchAgentDialogID = establishDialog(x)
      //mSearchTaskIdToQueryAgentDialogId += (taskID -> searchAgentDialogID)
      mSearchAgentDialogIdToSearchTaskId += (searchAgentDialogID -> taskID)
    }
  }

}

class Task(taskId: String) {
  val taskID = taskId
}

class SearchTask(taskId: String) extends Task(taskId) {
  var nrOfAnswersLeft = 0;
  val answers: Array[List[SingleSearchResult]] = Array(null, null)
  var nextFree: Int = 0;
  var query: String = null
}


