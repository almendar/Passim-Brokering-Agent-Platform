package pl.edu.pw.elka.stud.tkogut.brokering

import scala.Console
import scala.collection.mutable.Map
import pl.edu.pw.elka.stud.tkogut.sade.core._
import pl.edu.pw.elka.stud.tkogut.sade.core.yellowpages._
import pl.edu.pw.elka.stud.tkogut.sade.messages._
import pl.edu.pw.elka.stud.tkogut.brokering._
import pl.edu.pw.elka.stud.tkogut.brokering.messages._
import pl.edu.pw.elka.stud.tkogut.brokering.tools._
import scala.collection.mutable.ListBuffer

class BrokerAgent(nameOfAgent: String) extends Agent(nameOfAgent) {

  private var mSearchAgentsList: List[Agent] = null;

  private val mSearchAgentDialogIdToSearchTaskId = Map[String, String]()
  private val mSearchTaskIdToQueryAgentDialogId = Map[String, String]()

  /**
   * Maps dilogID with SearchAgents to associated task
   */
  private val mTasksMap = Map[String, SearchTask]()

  getSearchSources()

  override def handleMessage(msg: Message) {
    speak(msg)
    msg match {
      case res: SearchResultMessage => processSearchResult(res)
      case y: AgentList => saveSearchResources(y)
      case query: QueryMessage => addSearchTask(query) //establishDialog(searchAgentsList.head) //a.head ! Query(query.q, this) //() => { searchAgentsList.head ! Query(query.q, this) }
    }
  }

  override def processDialog(id: String) = {
    val dialog: Dialog = activeDialogs(id)
    val searchAgent = dialog.contact
    val taskId = mSearchAgentDialogIdToSearchTaskId(id)
    val searchTask = mTasksMap(taskId)
    val queryText: String = searchTask.query
    searchAgent ! new QueryMessage(queryText, id)
  }

  private def getSearchSources() = {

    val msg = new SendAgentsMeetingConstraint(this, (a: Agent) =>
      (a.isInstanceOf[SearchAgent]))
    YellowPagesAgent ! msg
  }

  private def processSearchResult(searchResults: SearchResultMessage) = {
    val st: SearchTask = mTasksMap(mSearchAgentDialogIdToSearchTaskId(searchResults.dialogID))
    st.nrOfAnswersLeft -= 1;
    st.answers.append(searchResults.resultsList)
    //speak(st.answers(st.nextFree).toString)
    if (st.nrOfAnswersLeft > 0) {
      st.nextFree += 1
    } else {
      speak("Got all answers for query:" + st.query)
      val merger = new SearchResultMerge()
      st.answers.foreach(merger.addResults(_))
      val askerDialogIdm = mSearchTaskIdToQueryAgentDialogId(st.taskID)
      val answerToQuery = new SearchResultMessage(this, askerDialogIdm)
      answerToQuery.resultsList = merger.getResultList
      activeDialogs(askerDialogIdm).contact ! answerToQuery
    }
  }

  private def saveSearchResources(lst: AgentList): Unit = {
    speak("Received list with search agents")
    if (lst.list.length != 0) mSearchAgentsList = lst.list
    else getSearchSources()
  }

  private def addSearchTask(queryMsg: QueryMessage) {

    import pl.edu.pw.elka.stud.tkogut.sade.core.Agent
    if (queryMsg.query.trim.isEmpty) {
      val dialogId = queryMsg.dialogId
      val who = activeDialogs(dialogId).contact
      activeDialogs -= queryMsg.dialogId
      val byeMsg = (Agent.BYE, dialogId)
      who ! byeMsg
    }

    val searchAgentsNumber = mSearchAgentsList.length
    val askerDialogID = queryMsg.dialogId
    val taskID = generateID()
    mSearchTaskIdToQueryAgentDialogId += (taskID -> askerDialogID)
    val task = new SearchTask(taskID)
    task.nrOfAnswersLeft = searchAgentsNumber
    task.query = queryMsg.query
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
  val answers: ListBuffer[List[SingleSearchResult]] = new ListBuffer[List[SingleSearchResult]]()
  var nextFree: Int = 0;
  var query: String = null
}


