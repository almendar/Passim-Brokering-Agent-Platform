package pl.edu.pw.ii.passim.util

import actors.Actor
import actors.Actor._

trait Producer[T] {

  case object Next
  case object Stop
  case object Undefined

  protected def produceAll: Unit

  protected def produce(x: T) {
    coordinator ! Some(x)
    producer.receive { case Next => }
  }

  private val producer: Actor = actor {
    receive {
      case Next =>
        produceAll
        coordinator ! None
    }
  }

  private val coordinator: Actor = actor {
    loop {
      react {
        case Next =>
          producer ! Next
          reply {
            receive { case x: Option[_] => x }
          }
        case Stop => exit('stop)
      }
    }
  }

  def iterator = new Iterator[T] {
    private var current: Any = Undefined

    private def lookAhead = {
      if (current == Undefined) current = coordinator !? Next
      current
    }

    def hasNext: Boolean = lookAhead match {
      case Some(x) => true
      case None => { coordinator ! Stop; false }
    }

    def next: T = lookAhead match {
      case Some(x) => current = Undefined; x.asInstanceOf[T]
    }
  }

}
