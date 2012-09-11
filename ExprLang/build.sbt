resolvers ++= Seq(
  "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "releases"  at "http://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.12.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.10.0" % "test"
)
 
