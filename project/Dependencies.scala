import sbt._

object Dependencies {
  val json4sVersion = "3.5.3"

  val jsonDependencies = Seq(
    "org.json4s" %% "json4s-native" % json4sVersion
  )

  val dependencies: Seq[ModuleID] = jsonDependencies
}