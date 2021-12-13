import sbt.Keys._
import sbt._

val appName = "secure"

val dependencies = Seq(
  "commons-codec"        %  "commons-codec"  % "1.15",
  "org.bouncycastle"     %  "bcprov-jdk15on" % "1.68",
  "org.scalatest"        %% "scalatest"      % "3.2.3"  % Test,
  "com.vladsch.flexmark" %  "flexmark-all"   % "0.36.8" % Test,
)

val scala2_12 = "2.12.15"
val scala2_13 = "2.13.7"

lazy val library = Project("secure", file("."))
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(
    scalaVersion := scala2_12,
    crossScalaVersions := Seq(scala2_12, scala2_13),
    majorVersion := 8,
    isPublicArtefact := true,
    libraryDependencies ++= dependencies,
    resolvers += Resolver.typesafeRepo("releases")
  )
  .settings(resolvers += Resolver.jcenterRepo)
