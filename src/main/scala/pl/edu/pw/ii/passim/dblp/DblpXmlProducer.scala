package pl.edu.pw.ii.passim.dblp

import java.io.InputStream
import xml.{XML, Node}
import pl.edu.pw.ii.passim.util.Producer

class DblpXmlProducer(is: InputStream) extends Producer[Node] {

  def produceAll {
    val parser = XML.parser
    val handler = DblpXmlHandler(produce)
    parser.parse(is, handler)
  }
  
}

object DblpXmlProducer {
  def iterator(is: InputStream) = (new DblpXmlProducer(is)).iterator
}
