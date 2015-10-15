lazy val commonSettings = Seq(
  organization := "com.kaihaosw",
  version := "0.1.0",
  scalaVersion := "2.11.7"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "RacingHorse",
    libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.13"
  )
