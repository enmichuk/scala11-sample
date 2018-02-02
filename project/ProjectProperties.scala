import sbt.Keys.{javacOptions, scalaVersion, version}
import sbt.{Build, Def}

object ProjectProperties extends Build {
  val Organization = "org.enmichuk"
  val NamePrefix = "scala11-sample-"
  val Version = "0.0.1-SNAPSHOT"
  val ProjectScalaVersion = "2.11.12"

  val commonSettings: Seq[Def.Setting[_]] = Seq(
    version := Version,
    scalaVersion := ProjectScalaVersion,
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8")
  )
}