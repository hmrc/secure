import sbt.Keys._
import sbt._
import uk.gov.hmrc.versioning.SbtGitVersioning
import uk.gov.hmrc.SbtArtifactory

val appName = "secure"

val dependencies = Seq(
  "commons-codec" % "commons-codec" % "1.8",
  "org.bouncycastle" % "bcprov-jdk15on" % "1.60",
  "org.scalatest" %% "scalatest"   % "3.0.1" % "test",
  "org.pegdown"   %  "pegdown"     % "1.6.0" % "test"
)

lazy val library = Project(appName, file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(
    scalaVersion := "2.12.12",
    majorVersion := 8,
    makePublicallyAvailableOnBintray := true,
    libraryDependencies ++= dependencies,
    resolvers := Seq(
      Resolver.bintrayRepo("hmrc", "releases"),
      Resolver.typesafeRepo("releases")
    )
  )
  .settings(resolvers += Resolver.jcenterRepo)
