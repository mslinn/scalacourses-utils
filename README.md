# ScalaCourses Scala Utils

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[ ![Download](https://api.bintray.com/packages/micronautics/scala/scalacourses-utils/images/download.svg) ](https://bintray.com/micronautics/scala/scalacourses-utils/_latestVersion)

## Installation ##
Add this resolver:

    "micronautics/scala on bintray" at "http://dl.bintray.com/micronautics/scala"

Add this dependency:

    "com.micronautics" %% "scalacourses-utils" % "0.3.5" withSources()

## Usage

### Reading from a File

````scala
import com.micronautics._

// Read lines into a multiline string
val multiLine1: String = utils.read("file.name")
val multiLine2: String = utils.read(new java.io.File("file.name"))

// Read lines into an List of String
val lines1: List[String] = utils.readLines("file.name")
val lines2: List[String] = utils.readLines(new java.io.File("file.name"))
````

### Using

````scala
import com.micronautics._

def readLines(file: File): List[String] =
    utils.using(new BufferedReader(new FileReader(file)))
       { reader => unfold(())(_ => Option(reader.readLine).map(_ -> ((): Unit))) }
````

### Enriched `Try`

````scala
import com.micronautics.utils.Implicits._

val x: Try[Int] = Try {
	2 / 0
} andThen {
	case Failure(ex) => println(s"Logging ${ex.getMessage}")
} andThen {
	case Success(value) => println(s"Success: got $value")
	case Failure(ex) => println(s"This just shows that any failure is provided to each chained andThen clause ${ex.getMessage}")
}
````

### Working with collections of `Try`:

````scala
import com.micronautics.utils.Implicits._

val tries = List(Try(6/0), Try("Happiness " * 3), Failure(new Exception("Drat!")), Try(99))

println(s"failures=${failures(tries).map(_.getMessage).mkString("; ")}")
println(s"successes=${successes(tries)}")
println(s"sequence=${sequence(tries)}")
val (successes, failures) = sequenceWithFailures(tries)
println(s"""sequenceWithFailures:
	successes=$successes
	failures=$failures""")
````

### Working with caches

````scala
import com.google.common.cache.CacheStats
import com.micronautics.cache._
import scala.concurrent.ExecutionContext.Implicits._

val softCache = SoftCache[String, Int]()
val x: Option[Int] = softCache.get("missing")
val y: Int = softCache.getWithDefault("2", 2))
cache.put("3", 3)
cache.remove("3")
val stats: CacheStats = cache.underlying.stats

val strongCache = StrongCache[String, String]()
val x: Option[String] = softCache.get("missing")
val y: String = softCache.getWithDefault("2", "two"))
cache.put("3", "three")
````

## See Also ##
The ScalaCourses [Intermediate Scala Course](http://www.scalacourses.com/student/showCourse/course_scalaIntermediate)
has several lectures on Scala Futures, and a full discussion of the contents of this library is provided in the
[Working With Collections of Futures](http://www.scalacourses.com/student/showLecture/177) lecture.
The [Partial Functions lecture](http://www.scalacourses.com/student/showLecture/88) discusses the `RichTry` class
and methods for working with collections of `Try`.

The course also provides a working SBT project that demonstrates this code.

The same course has a lecture on [Memoization in Depth](http://www.scalacourses.com/student/showLecture/200) which discusses the memoization code in detail.

## Scaladoc
[Here](http://mslinn.github.io/scalacourses-utils/latest/api/index.html)
