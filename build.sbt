import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.ryan",
      scalaVersion := "2.12.1",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "LangImplPatterns",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "org.antlr" % "antlr4-runtime" % "4.7"
  )
