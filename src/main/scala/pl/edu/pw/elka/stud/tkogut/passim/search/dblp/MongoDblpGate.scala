package pl.edu.pw.elka.stud.tkogut.passim.search.dblp

import com.mongodb.casbah._
import com.mongodb.casbah.commons.MongoDBObject

object MongoDatabase {
  final val DEFAULTR_PORT = 27017
  final val DEFAULT_HOST = "localhost"
}

class PeopleQuery {

}

class MongoDblpGate {
  private var connectionPort = MongoDatabase.DEFAULTR_PORT
  private var connectionHost = MongoDatabase.DEFAULT_HOST
  private var mongoConn: MongoConnection = null
  private var passimDB: MongoDB = null

  def documentsCollection = passimDB("documents")
  def peopleCollection = passimDB("people")

  def connect(adress: String = MongoDatabase.DEFAULT_HOST, port: Int = MongoDatabase.DEFAULTR_PORT) = {
    connectionHost = adress
    connectionPort = port
    mongoConn = MongoConnection(connectionHost, connectionPort)
    passimDB = mongoConn("passim")
  }

  private def keyValueRegexSearch(key: String, text: String) = {

    //TODO text need a lot of escaping!!!
    //Don't try to look for c++ :) it will fail gracefully

    val sb = new StringBuilder
    sb append "^"
    //sb append ".*"
    sb append text
    //sb append ".*"

    val qObj = MongoDBObject(key -> sb.mkString.r)
//    val qObj = MongoDBObject(key -> ("^"+ text.r))
    println(qObj)
    documentsCollection.find(qObj)
  }

  def lookForAuthors(text: String) = {
    keyValueRegexSearch("author", text)

  }

  def lookForPublications(text: String) = {
    keyValueRegexSearch("title", text)

  }

  def disconnect() = {
    mongoConn.close()
  }
}