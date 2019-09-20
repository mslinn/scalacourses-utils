import com.micronautics.cache._
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import org.scalatest._
import org.scalatest.Matchers._

@RunWith(classOf[JUnitRunner])
class TestMemos extends WordSpec {
  "Memoize trait" should {
    object MemoizeImpl extends Memoize {
      val compute: String => Unit = (_: String) => println("Compute1 was evaluated\n")
    }

    "work" in {
      val function1: String => Unit = MemoizeImpl.memoize[String, Unit](MemoizeImpl.compute)
      function1("blah") // should generate printed output
      function1("blah") // should not generate printed output
    }
  }

  "Memoizer classes" should {
    val _lazyBlock: String = {
      println("_lazyBlock was evaluated\n")
      "Constant value"
    }
    val lazyBlock: Memoizer0[String] = Memoizer(_lazyBlock)

    def _strLen(string: String): Int = string.length
    val strLen: Memoizer[String, Int] = Memoizer { _strLen }

    def _strConcat2(string1: String, string2: String): String = string1 + string2
    val strConcat2: Memoizer2[String, String, String] = Memoizer(_strConcat2)

    def _strConcat3(string1: String, string2: String, string3: String): String = string1 + string2 + string3
    val strConcat3: Memoizer3[String, String, String, String] = Memoizer(_strConcat3)

    "work" in {
      lazyBlock shouldEqual lazyBlock
      lazyBlock.clear()
      lazyBlock shouldEqual lazyBlock

      strLen("hi") shouldEqual strLen("hi")
      strLen.clear()
      strLen("hi") shouldEqual strLen("hi")

      strConcat2("x", "y") shouldEqual strConcat2("x", "y")
      strConcat2.clear()
      strConcat2("x", "y") shouldEqual strConcat2("x", "y")

      strConcat3("x", "y", "z") shouldEqual strConcat3("x", "y", "z")
      strConcat3.clear()
      strConcat3("x", "y", "z") shouldEqual strConcat3("x", "y", "z")
    }
  }
}
