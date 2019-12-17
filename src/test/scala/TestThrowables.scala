import com.micronautics.utils.Implicits._
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

class TestThrowables extends AnyWordSpec {
  "Throwables" should {
    "work" in {
      val fe1 = new Exception("message in a bottle").format()
      fe1 shouldEqual "message in a bottle"

      val fe2 = new Exception().format()
      fe2 should include ("TestThrowables")

      val fe3 = new Exception("message in a bottle").format(showStackTrace=true)
      fe3 should include ("message in a bottle")
      fe3 should include ("TestThrowables")

      val fe4 = new Exception("message in a bottle").format(asHtml=true)
      fe4 shouldEqual "message in a bottle"

      val fe5 = new Exception("message in a bottle").format(asHtml=true, showStackTrace=true)
      fe5 should include ("message in a bottle")
      fe5 should include ("TestThrowables")
      fe5 should include ("<pre>")
    }
  }
}
