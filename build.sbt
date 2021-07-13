ThisBuild / scalaVersion := "2.13.6"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

lazy val ver = new {
  val catsEffect   = "3.1.1"
  val circeVersion = "0.13.0"
  val couchbase    = "3.1.6"
  val log4cats     = "2.0.1"
  val logback      = "1.2.3"
  val pureconfig   = "0.14.1"
}

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    name := "couchbase-example",
    libraryDependencies ++= Seq(
      ("org.typelevel" %% "cats-effect" % ver.catsEffect).withSources().withJavadoc(),
      "com.github.pureconfig" %% "pureconfig"     % ver.pureconfig,
      "com.couchbase.client"  % "java-client"     % ver.couchbase,
      "io.circe"              %% "circe-core"     % ver.circeVersion,
      "io.circe"              %% "circe-generic"  % ver.circeVersion,
      "io.circe"              %% "circe-parser"   % ver.circeVersion,
      "org.typelevel"         %% "log4cats-core"  % ver.log4cats,
      "org.typelevel"         %% "log4cats-slf4j" % ver.log4cats,
      "ch.qos.logback"        % "logback-classic" % ver.logback
    ),
    Defaults.itSettings,
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.2" % "test,it",
      "org.testcontainers" % "couchbase" % "1.15.3" % IntegrationTest
    ),
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-unchecked",
      "-Ywarn-unused:imports"
    )
  )
  .enablePlugins(JavaAppPackaging)
