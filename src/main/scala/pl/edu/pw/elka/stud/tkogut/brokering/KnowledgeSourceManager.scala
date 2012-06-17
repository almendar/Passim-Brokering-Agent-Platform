package pl.edu.pw.elka.stud.tkogut.brokering

/**
 * Created with IntelliJ IDEA.
 * User: tomek
 * Date: 11.06.12
 * Time: 20:04
 * To change this template use File | Settings | File Templates.
 */


import collection.mutable.{ArrayBuffer, LinkedList}
import dialect.FreeTextSearch
import pl.edu.pw.elka.stud.tkogut.sade.core.Agent
import collection.mutable

/**
 * Class manages all knowledge sources that are registered within the Broker
 */
class KnowledgeSourceManager {

  private var agents = ArrayBuffer[SearchAgent]()

  def registerAgent(a:Agent*) {
    a.foreach
    {
      _ match
      {
        case x:SearchAgent => agents+=x
      }
    }
  }

  def registerAgent(a:Iterable[Agent]) {
    a.foreach
    {
      _ match
      {
        case x:SearchAgent => agents+=x
      }
    }
  }

  def getFreeSearchAgents() = {
    agents.filter {
      _.capabilities.contains(FreeTextSearch)
    }.toList
  }

}
