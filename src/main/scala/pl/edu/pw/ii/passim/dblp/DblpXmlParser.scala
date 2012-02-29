package pl.edu.pw.ii.passim.dblp

import org.xml.sax.helpers.DefaultHandler
import xml.parsing.NoBindingFactoryAdapter
import xml.Node
import org.xml.sax.Attributes

case class DblpXmlHandler(val collector: Node => Unit) extends DefaultHandler {

  var inside: Boolean = false
  var adapter = new NoBindingFactoryAdapter
  var tag = ""

  override def startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
    if (qName != "dblp") {
      if (!inside) {
        tag = qName
        adapter = new NoBindingFactoryAdapter()
        inside = true
      }
      adapter.startElement(uri, localName, qName, attributes)
    }
  }

  override def endElement(uri: String, localName: String, qName: String) {
    if (inside) {
      adapter.endElement(uri, localName, qName)
      if (qName == tag) {
        collector(adapter.rootElem)
        inside = false
      }
    }
  }

  override def characters(ch: Array[Char], start: Int, length: Int) {
    if (inside)
      adapter.characters(ch, start, length)
  }

  override def ignorableWhitespace(ch: Array[Char], start: Int, length: Int) {
    if (inside)
      adapter.characters(ch, start, length)
  }
}
