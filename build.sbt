name := "ScalaWithCats"

version := "0.1"

scalaVersion := "2.12.10"

libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0"

scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-Ypartial-unification"
)