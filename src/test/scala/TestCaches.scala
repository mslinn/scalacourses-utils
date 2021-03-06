import com.google.common.cache.CacheStats
import com.micronautics.cache._
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import scala.concurrent.ExecutionContext.Implicits._

class TestCaches extends AnyWordSpec {
  type Key = String
  type Value = String

  def defaultValue(key: Key): Value = s"$key value"

  "StrongCache" should {
    val cache = StrongCache[Key, Value](timeoutMinutes=5)
    "work" in {
      cache.get("missing") shouldEqual None
      cache.getWithDefault("2", defaultValue("2")) shouldEqual "2 value"
      cache.put("3", "three")
      cache.getWithDefault("3", defaultValue("2")) shouldEqual "three"
      cache.get("3") shouldEqual Some("three")

      val stats: CacheStats = cache.underlying.stats
      stats.hitCount shouldEqual 2
      stats.missCount shouldEqual 2
      stats.hitRate shouldEqual 0.5
      stats.requestCount shouldEqual 4

      cache.getAll.size shouldEqual 2
    }
  }

  "SoftCache" should {
    val cache = SoftCache[Key, Value]()
    "work" in {
      cache.get("missing") shouldEqual None
      cache.getWithDefault("2", defaultValue("2")) shouldEqual "2 value"
      cache.put("3", "three")
      cache.getWithDefault("3", defaultValue("2")) shouldEqual "three"
      cache.get("3") shouldEqual Some("three")
      cache.getAll.size shouldEqual 2

      cache.underlying.invalidateAll()
      cache.get("2") shouldEqual None

      cache.getAll.size shouldEqual 0
    }
  }
}
