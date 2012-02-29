package pl.edu.pw.ii.passim.dblp

import com.mongodb.casbah.Imports._

object DblpMongo {

  val mongoConn = MongoConnection()
  val db = mongoConn("passim")
  val documents = db("documents")
  val people = db("people")

  def loadData() {
    for (r <- DblpXml.allRecords) {
      val coll = r("type") match {
        case "person" => people
        case _ => documents
      }
      coll += r.asMap
    }
  }

  def main(args: Array[String]) {
    loadData()
    println("end")
  }
}
