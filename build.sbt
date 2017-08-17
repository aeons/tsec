name := "fucc"

version := "0.0.1"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats" % "0.9.0",
  "com.chuusai" %% "shapeless" % "2.3.2",
  "org.typelevel" %% "cats-effect" % "0.3",
  "commons-codec" % "commons-codec" % "1.10",
  "com.softwaremill.common" %% "tagging" % "2.1.0",
  "de.svenkubiak" % "jBCrypt" % "0.4.1",
  "com.lambdaworks" % "scrypt" % "1.4.0",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "co.fs2" %% "fs2-cats" % "0.3.0",
  "co.fs2" %% "fs2-core" % "0.9.6",
  "org.bitbucket.b_c" % "jose4j" % "0.6.0"
)
addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")

scalacOptions := Seq(
  "-unchecked",
  "-feature",
  "-deprecation",
  "-encoding",
  "utf8",
  "-Ypartial-unification",
  "-language:higherKinds",
  "-language:implicitConversions"
)
