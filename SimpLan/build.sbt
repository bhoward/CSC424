scalaVersion := "2.11.2"

EclipseKeys.withBundledScalaContainers:=false

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

// add scala-parser-combinators dependency when needed (for Scala 2.11 and newer) in a robust way
// this mechanism supports cross-version publishing
// taken from: http://github.com/scala/scala-module-dependency-sample
libraryDependencies := {
  CrossVersion.partialVersion(scalaVersion.value) match {
    // if scala 2.11+ is used, add dependency on scala-xml module
    case Some((2, scalaMajor)) if scalaMajor >= 11 =>
      libraryDependencies.value ++ Seq(
        "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.1",
        "org.scala-lang.modules" %% "scala-swing" % "1.0.1"
      )
    case _ =>
      libraryDependencies.value :+ "org.scala-lang" % "scala-swing" % scalaVersion.value
  }
}

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.4.2" % "test",
  "org.scalacheck" %% "scalacheck" % "1.10.0" % "test"
)
 
scalacOptions in Test ++= Seq("-Yrangepos")

mainClass in assembly := Some("csc424.GUIMain")

assemblyJarName in assembly := "SimpLan.jar"
