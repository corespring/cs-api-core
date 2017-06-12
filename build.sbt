import sbt._
import sbt.Keys._
import org.corespring.sbt.repo.RepoAuthPlugin.Keys._
import Dependencies._

resolvers in ThisBuild ++= Seq(
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/",
  "Corespring releases" at "http://repository.corespring.org/artifactory/ivy-releases/"
)

fork in Test := true

organization in ThisBuild := "org.corespring"
scalaVersion in ThisBuild := "2.10.5"

lazy val builders = new Builders("corespring", Seq.empty)


lazy val coreModels = builders.lib("models", publish = true).settings(
    libraryDependencies ++= Seq(casbah, salatVersioningDao, playJson, commonsLang, specs2 % "test"))

  lazy val coreJson = builders.lib("json").dependsOn(coreModels)
    .settings(libraryDependencies ++= Seq(specs2 % "test"))

  lazy val futureValidation = builders.lib("future-validation", publish = true)
    .settings(libraryDependencies ++= Seq(scalaz, specs2 % "test"))

  lazy val coreServices = builders.lib("services", publish = true)
    .settings(
      libraryDependencies ++= Seq(specs2 % "test"))
    .dependsOn(coreModels, futureValidation)

  lazy val coreUtils = builders.lib("utils", publish = true)
    .settings(
      libraryDependencies ++= Seq(specs2 % "test"))

  lazy val coreSalatConfig = builders.lib("salat-config", publish = true).settings(
    libraryDependencies ++= Seq(salat))

  lazy val coreServicesSalat = builders.lib("services-salat", publish = true)
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

  lazy val encryption = builders.lib("encryption", publish = true)
    .settings(libraryDependencies ++= Seq(casbah, commonsCodec, macWireMacro, jbcrypt, specs2 % "test"))
    .dependsOn(coreServices, coreModels)


lazy val root = Project("cs-api-core", file("."))
.settings(
  publishTo := authPublishTo.value,
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
  encryption
) .configs(IntegrationTest)