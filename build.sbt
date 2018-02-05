import ProjectProperties._
import Dependencies._

name := NamePrefix + "root"
organization := Organization
version := Version
scalaVersion := ProjectScalaVersion

lazy val core = project
  .settings(commonSettings)
  .settings(libraryDependencies ++= dependencies)

lazy val json = project
  .settings(commonSettings)
  .settings(libraryDependencies ++= dependencies)

lazy val http = project
  .settings(commonSettings)
  .settings(libraryDependencies ++= dependencies)

lazy val root = project.in( file(".") )
  .settings(commonSettings)
  .aggregate(json, core, http)
