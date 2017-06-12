import Dependencies._
import sbt.Keys._
import sbt._

resolvers in ThisBuild ++= Seq(
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/",
  "Corespring releases" at "http://repository.corespring.org/artifactory/ivy-releases/"
)

fork in Test := true

organization in ThisBuild := "org.corespring"
scalaVersion in ThisBuild := "2.10.5"

lazy val builders = new Builders("core", Seq.empty)


lazy val coreModels = builders.lib("models").settings(
  libraryDependencies ++= Seq(casbah, salatVersioningDao, playJson, commonsLang, specs2 % "test"))

lazy val coreJson = builders.lib("json").dependsOn(coreModels)
  .settings(libraryDependencies ++= Seq(specs2 % "test"))

lazy val futureValidation = builders.lib("future-validation")
  .settings(libraryDependencies ++= Seq(scalaz, specs2 % "test"))

lazy val coreServices = builders.lib("services")
  .settings(
    libraryDependencies ++= Seq(specs2 % "test"))
  .dependsOn(coreModels, futureValidation)

lazy val coreUtils = builders.lib("utils")
  .settings(
    libraryDependencies ++= Seq(specs2 % "test"))

lazy val coreSalatConfig = builders.lib("salat-config").settings(
  libraryDependencies ++= Seq(salat))

lazy val coreServicesSalat = builders.lib("services-salat")
  .settings(
    libraryDependencies ++= Seq(salat, salatVersioningDao, grizzledLog, logbackClassic, aws, corespringMacros))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .settings(
    Keys.parallelExecution in IntegrationTest := false,
    Keys.fork in IntegrationTest := false,
    Keys.logBuffered := false,
    testOptions in IntegrationTest += Tests.Setup((loader: java.lang.ClassLoader) => {
      loader.loadClass("org.corespring.services.salat.it.Setup").newInstance
    }),
    testOptions in IntegrationTest += Tests.Cleanup((loader: java.lang.ClassLoader) => {
      loader.loadClass("org.corespring.services.salat.it.Cleanup").newInstance
    }),
    testOptions in IntegrationTest += Tests.Setup(() => println("---------> Setup Integration Test")),
    testOptions in IntegrationTest += Tests.Cleanup(() => println("-----------> Cleanup Integration Test")))
  .settings(libraryDependencies ++= Seq(macWireMacro, macWireRuntime, specs2 % "it,test", aws))
  .dependsOn(coreSalatConfig, coreServices, coreUtils)

lazy val encryption = builders.lib("encryption")
  .settings(libraryDependencies ++= Seq(casbah, commonsCodec, macWireMacro, jbcrypt, specs2 % "test"))
  .dependsOn(coreServices, coreModels)


lazy val root = Project("cs-api-core", file("."))
  .settings(
    publishArtifact := false,
    publish := (),
    publishLocal := (),
    parallelExecution in IntegrationTest := false
  ).dependsOn(
  coreModels,
  coreJson,
  coreServices,
  coreUtils,
  coreSalatConfig,
  coreServicesSalat,
  encryption
).aggregate(
  coreModels,
  coreJson,
  coreServices,
  coreUtils,
  coreSalatConfig,
  coreServicesSalat,
  encryption,
  futureValidation
)
  .configs(IntegrationTest)
