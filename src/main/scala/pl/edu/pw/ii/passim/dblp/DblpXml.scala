package pl.edu.pw.ii.passim.dblp

import java.util.zip.GZIPInputStream
import xml.Node
import java.io.FileInputStream

object DblpXml {

  val inputFileName = "data/dblp.xml.gz"

  def makeMap(node: Node): ObjectMap = {
    var map =new ObjectMap
    map = map.addBinding("type", node.label)
    for (a <- node.attributes) {
      map = map.addBinding(a.key, a.value.toString)
    }
    for (n <- node.child) {
      if (!n.isAtom)
        map = map.addBinding(n.label, n.text.toString)
    }
    map
  }

  def detectPersonRecords(map: ObjectMap) = {
    if (map("type") == "www" && map("key").asInstanceOf[String].startsWith("homepage"))
      (map - "type").addBinding("type", "person") - "title"
    else
      map
  }

  def allRecords = {
    System.setProperty("entityExpansionLimit", "2500000")
    val is = new FileInputStream(inputFileName)
    val gis = new GZIPInputStream(is)

    DblpXmlProducer.iterator(gis)
      .map(makeMap)
      .map(detectPersonRecords)
  }
}
