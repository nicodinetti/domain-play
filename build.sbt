import sbt.Keys._
import sbtrelease._

import ReleaseStateTransformations._

import net.virtualvoid.sbt.graph.Plugin._

// ··· Settings ···

graphSettings

releaseSettings

// ··· Project Info ···

name := "domain-play"

organization := "com.despegar.domain"

crossScalaVersions := Seq("2.10.4", "2.11.1")

crossVersion := CrossVersion.binary

fork in run   := true

// ··· Project Enviroment ···

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

EclipseKeys.projectFlavor := EclipseProjectFlavor.Scala

EclipseKeys.executionEnvironment := Some(EclipseExecutionEnvironment.JavaSE17)

unmanagedSourceDirectories in Compile := (scalaSource in Compile).value :: Nil

unmanagedSourceDirectories in Test := (scalaSource in Test).value :: Nil

resourceDirectory in Compile := baseDirectory.value / "src" / "main" / "resources"


// ··· Project Options ···

javacOptions ++= Seq(
    "-source", "1.7",
    "-target", "1.7"
)

scalacOptions ++= Seq(
    "-encoding",
    "utf8",
    "-feature",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-unchecked",
    "-deprecation"
)

// ··· Project Repositories ···

publishTo := {
    val nexus = "http://nexus.despegar.it:8080/nexus/content/repositories/"
    if (version.value.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at nexus + "snapshots")
    else
      Some("releases"  at nexus + "releases")
}

resolvers ++= Seq(
    "Nexus Public Repository"        at "http://nexus.despegar.it:8080/nexus/content/groups/public",
    "Nexus Snapshots Repository"     at "http://nexus.despegar.it:8080/nexus/content/repositories/snapshots",
    "Nexus Releases Repository"      at "http://nexus.despegar.it:8080/nexus/content/repositories/releases",
    "OSS"                            at "http://oss.sonatype.org/content/repositories/releases/")

// ··· Project Dependancies···

libraryDependencies ++= Seq(
  // --- Config ---
  "com.typesafe"                  %  "config"                % "1.2.1",
  // --- Core ---
  "com.despegar.domain"           %% "domain-scala"          % "2.1" ,
  // --- Play ---
  "com.typesafe.play"             %% "play"                  % "2.3.0"   %  "provided",
  "com.typesafe.play"             %% "play-json"             % "2.3.0"   %  "provided",
  // --- Utils ---
  "org.apache.commons"            %  "commons-lang3"         % "3.3.2",
  // --- Testing ---
  "org.specs2"                    %% "specs2-core"           % "2.3.12"  % "test",
  "org.specs2"                    %% "specs2-junit"          % "2.3.12"  % "test",
  "junit"                         %  "junit"                 % "4.11"    % "test"
)
