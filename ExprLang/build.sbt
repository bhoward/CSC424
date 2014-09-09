scalaVersion := "2.11.2"

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.4.2" % "test",
  "org.scalacheck" %% "scalacheck" % "1.10.0" % "test"
)
 
scalacOptions in Test ++= Seq("-Yrangepos")
