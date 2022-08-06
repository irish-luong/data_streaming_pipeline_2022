name := "apps"

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "streaming"
  )

val sparkVersion = "3.2.0"
libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVersion
libraryDependencies += "org.apache.kafka" % "kafka-clients" % sparkVersion
libraryDependencies +=  "org.apache.hadoop" % "hadoop-client" % sparkVersion
