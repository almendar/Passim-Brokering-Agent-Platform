package pl.edu.pw.elka.stud.tkogut.passim.search
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfterAll
import pl.edu.pw.elka.stud.tkogut.passim.search.dblp.MongoDblpGate
import com.mongodb.casbah.commons.MongoDBObject
import java.util.ArrayList
import com.mongodb.BasicDBList

class DblpMongoTests extends FunSuite with BeforeAndAfterAll {

  val mongoGate = new MongoDblpGate()
  override def beforeAll() = {
    mongoGate.connect()
  }

  override def afterAll() = mongoGate.disconnect

  test("Title text search") {
    //val quer = MongoDBObject("author" -> ".*Tomasz.*".r)
    val res = mongoGate.lookForPublications("Automotive")
    assert(res.hasNext)
    val firstResult = res.next
    assert(firstResult.containsField("title"))
    firstResult.get("title") match {
      case lst: BasicDBList => assert(lst.toArray().exists(_.asInstanceOf[String].contains("Automotive")))
      case str: String => assert(str.contains("Automotive"))
      case _ => assert(false)
    }
  }

  test("Author text search") {
    val name = "Agrawal"
    val field = "author"
    //val quer = MongoDBObject("author" -> ".*Tomasz.*".r)
    val res = mongoGate.lookForAuthors(name)
    assert(res.hasNext)
    val firstResult = res.next
    assert(firstResult.containsField(field))

    firstResult.get(field) match {
      case lst: BasicDBList => {
        assert(lst.toArray().exists(_.asInstanceOf[String].contains(name)))
        //        assert(lst.toArray[String]().exists { _.asInstanceOf[String].contains(name) })
      }
      case s: String => assert(s.contains(name))
      case _ => assert(false)
    }

    //(.asInstanceOf[String].contains(name))
  }

}