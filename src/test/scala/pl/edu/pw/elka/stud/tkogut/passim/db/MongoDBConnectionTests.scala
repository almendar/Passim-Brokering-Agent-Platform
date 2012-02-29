package pl.edu.pw.elka.stud.tkogut.passim.db
import org.scalatest.FunSuite
import com.mongodb.casbah.Imports._
import org.scalatest.BeforeAndAfterAll
import com.mongodb.DBObject

class MongoDBConnectionTests extends FunSuite with BeforeAndAfterAll {

  val NAME = "Name"
  val SURNAME = "Surname"
  val AGE = "AGE"

  def createPerson(name: String, surname: String, age: Int) = {
    MongoDBObject(NAME -> name,
      SURNAME -> surname,
      AGE -> age)

  }

  val people: List[MongoDBObject] = List(
    createPerson("Jon", "Doe", 23),
    createPerson("Anthony", "Soprano", 47),
    createPerson("Michael", "Corleone", 29),
    createPerson("Fredo", "Corleone", 37))

  override def beforeAll() {
    people.foreach(x => trash += x)
  }

  override def afterAll() {
    people.foreach(x => trash -= x)
  }

  val mongo = MongoConnection()
  val trash = mongo("db_test")("trash")

  test("Searching by surname") {

    val q = MongoDBObject(SURNAME -> "Corleone")
    val result = for (x <- trash.find(q)) yield x
    assert(result.length == 2)
  }

  test("Search by age") {
    val q: MongoDBObject = (AGE $gt 19 $lt 30) //people in 30'es
    val result = for (x <- trash.find(q)) yield x
    assert(result.length == 2)
  }

  test("Query with id") {
    val person = trash.findOne(MongoDBObject(SURNAME -> "Soprano"))
    val query_id = person.getOrElse(MongoDBObject("_id" -> 0))("_id")
    val personAlt = trash.findOneByID(query_id)
    assert(personAlt == person)
  }

}