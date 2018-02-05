import sbt._

object Dependencies {
  val json4sVersion = "3.5.3"
  val jacksonVersion = "2.8.8"
  val circeVersion = "0.8.0"
  val httpclientVersion = "4.5.2"

  val jsonDependencies = Seq(
    "org.json4s" %% "json4s-native" % json4sVersion,
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion,
    "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion,
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic-extras" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-java8" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "io.circe" %% "circe-literal" % circeVersion
  )

  val httpDependencies = Seq(
    "org.apache.httpcomponents" % "httpclient" % httpclientVersion,
    "org.apache.httpcomponents" % "httpclient" % httpclientVersion classifier "tests"
  )

  val utilDependencies = Seq(
    "commons-io" % "commons-io" % "2.1"
  )

  val testDependencies = Seq(
    "org.scalatest" %% "scalatest" % "2.2.6",
    "org.mockito" % "mockito-all" % "1.10.19"
  )

  val dependencies: Seq[ModuleID] = jsonDependencies ++ httpDependencies ++ utilDependencies ++ testDependencies
}