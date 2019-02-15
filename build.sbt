import sbt.Keys._
import sbt._
import uk.gov.hmrc.versioning.SbtGitVersioning

val appName = "secure"

val dependencies = Seq(
  "commons-codec" % "commons-codec" % "1.8",
  "org.bouncycastle" % "bcprov-jdk15on" % "1.60",
  "org.scalatest" %% "scalatest"   % "3.0.1" % "test",
  "org.pegdown"   %  "pegdown"     % "1.6.0" % "test"
)

lazy val library = Project(appName, file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(
    crossScalaVersions := Seq("2.12.8", "2.11.11"),
    majorVersion := 7,
    makePublicallyAvailableOnBintray := true,
    libraryDependencies ++= dependencies,
    resolvers := Seq(
      Resolver.bintrayRepo("hmrc", "releases"),
      Resolver.typesafeRepo("releases")
    )
  )
  .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)
