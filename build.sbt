import ProjectProperties._
import Dependencies._

name := NamePrefix + "root"
organization := Organisation
version := Version
scalaVersion := ProjectScalaVersion

lazy val json = project
  .settings(commonSettings)
  .settings(libraryDependencies ++= dependencies)

lazy val root = project.in( file(".") ).settings(commonSettings).aggregate(json)
