cancelable := true

crossScalaVersions := Seq("2.10.6", "2.11.12", "2.12.10", "2.13.1")

developers := List(
  Developer("mslinn",
            "Mike Slinn",
            "mslinn@micronauticsresearch.com",
            url("https://github.com/mslinn")
  )
)

// define the statements initially evaluated when entering 'console', 'console-quick', but not 'console-project'
initialCommands in console := """import scala.util.{Failure, Success, Try}
                                |import com.micronautics.utils.Implicits._
                                |""".stripMargin

javacOptions ++= Seq(
  "-Xlint:deprecation",
  "-Xlint:unchecked",
  "-source", "1.8",
  "-target", "1.8",
  "-g:vars"
)

libraryDependencies ++= Seq(
  "com.google.guava" %  "guava"     % "28.0-jre" withSources(),
  //
  "org.scalatest"    %% "scalatest" % "3.1.0" % Test withSources(),
  "junit"            %  "junit"     % "4.12"  % Test
)

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

logLevel := Level.Warn

// Only show warnings and errors on the screen for compilations.
// This applies to both test:compile and compile and is Info by default
logLevel in compile := Level.Warn

// Level.INFO is needed to see detailed output when running tests
logLevel in test := Level.Info

name := "scalacourses-utils"

organization := "com.micronautics"

scalacOptions ++= (
  scalaVersion {
    case sv if sv.startsWith("2.10") => Nil
    case _ => List(
      "-target:jvm-1.8",
      "-Ywarn-unused"
    )
  }.value ++ Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked",
    "-Xlint"
  )
)

scalacOptions in (Compile, doc) ++= baseDirectory.map {
  bd: File => Seq[String](
     "-sourcepath", bd.getAbsolutePath,
     "-doc-source-url", "https://github.com/mslinn/scalacourses-utils/tree/master€{FILE_PATH}.scala"
  )
}.value

scalacOptions in Test ++= Seq("-Yrangepos")

scalaVersion := "2.13.1"

scmInfo := Some(
  ScmInfo(
    url(s"https://github.com/mslinn/$name"),
    s"git@github.com:mslinn/$name.git"
  )
)

version := "0.3.5"
