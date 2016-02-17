import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._

name := """CheckIn"""

version := "1.4-SNAPSHOT"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,
  "mysql" % "mysql-connector-java" % "5.1.38",
  "com.datastax.cassandra" % "cassandra-driver-core" % "3.0.0-beta1",
  "org.webjars" % "angular-ui-router" % "0.2.15",
  "org.webjars" % "angular-ui-bootstrap" % "0.13.3",
  "org.webjars" % "angular-toastr" % "1.3.0",
  "org.webjars" % "angularjs" % "1.4.3",
  "org.webjars" % "bootstrap" % "3.3.5",
  "me.lessis" %% "courier" % "0.1.3",
  "org.webjars.bower" % "angular-websocket" % "1.0.14",
  "com.typesafe.play" %% "anorm" % "2.4.0"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers ++= Seq("scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "softprops-maven" at "http://dl.bintray.com/content/softprops/maven")

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

maintainer := "darioalessandro"

dockerExposedPorts in Docker := Seq(9000)

dockerRepository := Some("darioalessandro")



