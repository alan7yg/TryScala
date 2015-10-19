lazy val commonSettings = Seq(
  organization := "com.kaihaosw",
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.11.7"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "DistributedRacingHorse",
    libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.13",
    libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.3.13"
  )
