import com.micronautics.utils
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatestplus.junit.JUnitRunner
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

@RunWith(classOf[JUnitRunner])
class TestRead extends AnyWordSpec with Matchers {
  val fileName = "src/main/scala/com/micronautics/utils/package.scala"

  "read" should {
    "work" in {
      val packageScala = utils.read(fileName)
      packageScala should include("readLines")

      val packageScalaLines = utils.readLines(fileName)
      packageScalaLines.length should be >= 10
    }
  }
}
