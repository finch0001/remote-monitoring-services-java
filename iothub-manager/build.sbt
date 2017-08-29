// Copyright (c) Microsoft. All rights reserved.

name := "iothub-manager"
organization := "com.microsoft.azure.iotsolutions"

scalaVersion := "2.12.2"

libraryDependencies ++= {
  Seq(
    filters,
    guice,

    // https://github.com/Azure/azure-iot-sdk-java/releases
    "com.microsoft.azure.sdk.iot" % "iot-service-client" % "1.5.22"
  )
}

lazy val commonSettings = Seq(
  version := "0.1.1",

  organizationName := "Microsoft Azure",
  organizationHomepage := Some(new URL("https://www.microsoft.com/internet-of-things/azure-iot-suite")),
  homepage := Some(new URL("https://www.microsoft.com/internet-of-things/azure-iot-suite")),
  startYear := Some(2017),

  // Assembly
  assemblyMergeStrategy in assembly := {
    case m if m.startsWith("META-INF") ⇒ MergeStrategy.discard
    case m if m.contains(".txt")       ⇒ MergeStrategy.discard
    case x                             ⇒ (assemblyMergeStrategy in assembly).value(x)
  },

  // Publishing options, see http://www.scala-sbt.org/0.13/docs/Artifacts.html
  licenses += ("MIT", url("https://github.com/Azure/pcs-template-microservice-java/blob/master/LICENSE")),
  publishMavenStyle := true,
  publishArtifact in Test := true,
  publishArtifact in(Compile, packageDoc) := true,
  publishArtifact in(Compile, packageSrc) := true,
  publishArtifact in(Compile, packageBin) := true,

  // Test
  testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v")),

  // Misc
  logLevel := Level.Info, // Debug|Info|Warn|Error
  scalacOptions ++= Seq("-deprecation", "-explaintypes", "-unchecked", "-feature"),
  showSuccess := true,
  showTiming := true,
  logBuffered := false,
  fork := true,
  parallelExecution := true
)

// Main module
lazy val iothubmanager = project.in(file("."))
  .enablePlugins(PlayJava)
  .configs(IntegrationTest)
  .settings(commonSettings)

// Play framework
PlayKeys.externalizeResources := false

// Docker
// Note: use lowercase name for the Docker image details
enablePlugins(JavaAppPackaging)
dockerRepository := Some("azureiotpcs")
dockerAlias := DockerAlias(dockerRepository.value, None, packageName.value + "-java", Some((version in Docker).value))
maintainer in Docker := "Devis Lucato (https://github.com/dluc)"
dockerBaseImage := "toketi/openjdk-8-jre-alpine-bash"
dockerUpdateLatest := true
dockerBuildOptions ++= Seq("--squash", "--compress", "--label", "Tags=toketi,dluc")
// Example params: -Dconfig.file=/opt/conf/prod.conf -Dhttp.port=1234 -Dhttp.address=127.0.0.1
dockerEntrypoint := Seq("bin/iothub-manager")