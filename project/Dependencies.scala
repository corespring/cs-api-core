import sbt._
object Dependencies {

  val playVersion = "2.2.6"

  val qtiVersion = "0.16"

  object ModuleConfigurations {

    import org.corespring.sbt.repo.RepoAuthPlugin

    val chainedSnapshots = ChainedResolver("chained", Seq(Resolver.defaultLocal, RepoAuthPlugin.snapshots))
    val snapshots = ModuleConfiguration("org.corespring", "*", "^.*?-SNAPSHOT$", chainedSnapshots)
    val releases = ModuleConfiguration("org.corespring", "*", "^0\\.\\d\\d$", RepoAuthPlugin.releases)
  }

  val aws = "com.amazonaws" % "aws-java-sdk" % "1.10.0"
  val casbah = "org.mongodb" %% "casbah" % "2.6.3"
  val commonsCodec = "commons-codec" % "commons-codec" % "1.10"
  val commonsLang = "org.apache.commons" % "commons-lang3" % "3.2.1"
  val corespringMacros = "org.corespring" %% "macros" % "1.1.0"
  val grizzledLog = "org.clapper" %% "grizzled-slf4j" % "1.0.2"
  val jbcrypt = "org.mindrot" % "jbcrypt" % "0.3m"
  val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.1.3"
  val macWireMacro = "com.softwaremill.macwire" %% "macros" % "0.7.3"
  val macWireRuntime = "com.softwaremill.macwire" %% "runtime" % "0.7.3"
  val mockito = "org.mockito" % "mockito-all" % "1.9.5" % "test"
  val playJson = "com.typesafe.play" %% "play-json" % playVersion //exclude("org.scala-stm", "scala-stm_2.10.0")
  val qti = "org.corespring" %% "corespring-qti" % qtiVersion
  val salat = "com.novus" %% "salat" % "1.9.4"
  val salatVersioningDao = "org.corespring" %% "salat-versioning-dao" % "0.21.0"
  val scalaz = "org.scalaz" %% "scalaz-core" % "7.0.6"
  val slf4j = "org.slf4j" % "slf4j-api" % "1.7.5"
  val specs2 = "org.specs2" %% "specs2" % "2.2.2" // "3.6.2"

  object Resolvers {

    val corespringPublicSnapshots = "Corespring Public Artifactory Snapshots" at "http://repository.corespring.org/artifactory/public-ivy-snapshots"
    val ivyLocal = Resolver.file("ivyLocal", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns)
    val sbtPluginReleases = Resolver.url("sbt-plugin-snapshots", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-snapshots/"))(Resolver.ivyStylePatterns)
    val sbtPluginSnapshots = Resolver.url("sbt-plugin-releases", url("http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)
    val scalazBintray = "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
    val sonatypeReleases = "Sonatype OSS" at "https://oss.sonatype.org/content/repositories/releases"
    val sonatypeSnapshots = "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
    val typesafe = "typesafe releases" at "http://repo.typesafe.com/typesafe/releases/"

    val all: Seq[Resolver] = Seq(
      scalazBintray,
      sonatypeSnapshots,
      typesafe,
      corespringPublicSnapshots,
      sbtPluginSnapshots,
      sbtPluginReleases,
      ivyLocal)
  }

}
