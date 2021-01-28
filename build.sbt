import sbt.Keys._
import sbt._
import uk.gov.hmrc.versioning.SbtGitVersioning
import uk.gov.hmrc.SbtArtifactory

val appName = "secure"

val dependencies = Seq(
  "commons-codec"        %  "commons-codec"  % "1.15",
  "org.bouncycastle"     %  "bcprov-jdk15on" % "1.68",
  "org.scalatest"        %% "scalatest"      % "3.2.3"  % Test,
  "com.vladsch.flexmark" %  "flexmark-all"   % "0.36.8" % Test,
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
