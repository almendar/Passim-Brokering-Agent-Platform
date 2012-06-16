package pl.edu.pw.elka.stud.tkogut.brokering

import scala.collection.mutable.Map
import scala.collection.mutable.ListBuffer
import pl.edu.pw.elka.stud.tkogut.sade.core._
import pl.edu.pw.elka.stud.tkogut.sade.core.yellowpages._
import pl.edu.pw.elka.stud.tkogut.sade.messages._
import pl.edu.pw.elka.stud.tkogut.brokering.messages._
import pl.edu.pw.elka.stud.tkogut.brokering.tools._


class BrokerAgent(nameOfAgent: String) extends Agent(nameOfAgent) {

  private var mSearchAgentsList: List[Agent] = null;

  /* maps dialogId established with search agent to search task id */
  private val mSearchAgentDialogIdToSearchTaskId = Map[String, String]()

  /* maps search task id to dialog id with query agent */
  private val mSearchTaskIdToQueryAgentDialogId = Map[String, String]()

  /**
   */
  private val mTasksMap = Map[String, SearchTask]()

  getSearchSources()

  override def handleMessage(msg: Message) {
    msg match {
      case res: SearchResultMessage => processSearchResult(res)
      case y: AgentListQueryMessage => saveSearchResources(y)
      case query: QueryMessage => addSearchTask(query) //establishDialog(searchAgentsList.head) //a.head ! Query(query.q, this) //() => { searchAgentsList.head ! Query(query.q, this) }
    }
  }

  override def processDialog(id: String) = {
    val searchAgent = dialogMgr.getContact(id)
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
      dialogMgr.getContact(askerDialogIdm) ! answerToQuery
    }
  }

  private def saveSearchResources(lst: AgentListQueryMessage): Unit = {
    speak("Received list with search agents")
    if (lst.list.length != 0) mSearchAgentsList = lst.list
    else getSearchSources()
  }

  private def addSearchTask(queryMsg: QueryMessage) {

    import pl.edu.pw.elka.stud.tkogut.sade.core.Agent
    if (queryMsg.query.trim.isEmpty) {

      sayGoodbye(queryMsg.dialogId)
    }
    val searchAgentsNumber = mSearchAgentsList.length
    val askerDialogID = queryMsg.dialogId
    val taskID = DialogManager.generateID()
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


  def sayGoodbye(dialogId: String) {
    val who = dialogMgr.getContact(dialogId)
    dialogMgr.removeDialog(dialogId)
    val byeMsg = (Agent.BYE, dialogId)
    who ! byeMsg
  }
}






