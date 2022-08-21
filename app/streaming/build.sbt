name := "apps"

// include the 'provided' Spark dependency on the classpath for `sbt run`
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val sparkVersion = "3.2.0"

lazy val root = (project in file("."))
  .settings(
    name := "streaming"
  )

libraryDependencies ++= Seq(
  ("org.apache.spark" %% "spark-sql" % sparkVersion),
  ("org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVersion),
  ("org.apache.spark" %% "spark-core" % sparkVersion),
  ("org.apache.kafka" % "kafka-clients" % sparkVersion),
  ("org.apache.hadoop" % "hadoop-client" % sparkVersion)
)

// Scala testing modules
libraryDependencies += "org.scalatest" %% "scalatest" % "3.3.0-SNAP3" % Test


// Logging
libraryDependencies += "org.apache.logging.log4j" % "log4j-api" % "2.18.0"
libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.18.0"

//Compile / run := Defaults.runTask(Compile / fullClasspath, Compile / run / mainClass, Compile / run / runner).evaluated
