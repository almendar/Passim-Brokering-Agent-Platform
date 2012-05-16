package pl.edu.pw.ii.passim.dblp

case class ObjectMap(map: Map[String, Any] = Map.empty) {

  def addBinding(k: String, v: Any): ObjectMap = {
    val r = map.get(k)
    val nv = r match {
      case None => v
      case Some(head :: tail) => v :: (head :: tail)
      case Some(x) => List(v, x)
    }
    ObjectMap(map + (k -> nv))
  }

  def removeBinding(k: String, v: Any): ObjectMap = {
    val r = map.get(k)
    val nv = r match {
      case None => v
      case Some(x) if x.isInstanceOf[List[Any]] => x.asInstanceOf[List[Any]].filterNot(_ == v)
      case Some(x) if x ==v => Nil
      case x => x
    }
    if (nv == Nil)
      this - k
    else
      ObjectMap(map + (k -> nv))
  }

  def -(k: String) = ObjectMap(map -  k)

  def apply(k: String) = map(k)

  override def toString: String = toString(0)

  def toString(indent: Int): String = {
    map.map { case(k, v) => makeString(k, v, indent) }.mkString("\n")
  }

  private def makeString(k: String, v: Any, indent: Int): String = {
    val heading = (" " * indent) + k + ": "
    v match {
        case m: ObjectMap => heading + "\n" + m.toString(indent + 2)
        case xs: List[_] =>
          if (xs.exists(x => x.isInstanceOf[ObjectMap] || x.isInstanceOf[List[_]]))
            xs.map(makeString(k, _, indent)).mkString("\n")
          else
            heading + xs.reverse.mkString(", ")
        case x => heading + x
    }
  }

  def asMap: Map[String, Any] = {
    map.mapValues {
      case x: ObjectMap => x.asMap
      case x: List[Any] => x.reverse
      case x => x
    }
  }
}
