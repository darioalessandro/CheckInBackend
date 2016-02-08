name := """CheckIn"""

version := "1.2-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,
  "com.datastax.cassandra" % "cassandra-driver-core" % "3.0.0-beta1"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

resolvers += "softprops-maven" at "http://dl.bintray.com/content/softprops/maven"



libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,
  "org.webjars" % "angular-ui-router" % "0.2.15",
  "org.webjars" % "angular-ui-bootstrap" % "0.13.3",
  "org.webjars" % "angular-toastr" % "1.3.0",
  "org.webjars" % "angularjs" % "1.4.3",
  "org.webjars" % "bootstrap" % "3.3.5",
  "me.lessis" %% "courier" % "0.1.3",
  "org.webjars.bower" % "angular-websocket" % "1.0.14"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator


