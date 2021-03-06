import sbt._
import Keys._

object TheiaBuild extends Build {
  val projectName = "Theia"

  def extraResolvers = Seq(
    resolvers ++= Seq(
      "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
      "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"))

  def extraLibraryDependencies = Seq(
    libraryDependencies ++= Seq(
      "org.scalanlp" %% "breeze" % "0.11.2",
      "commons-io" % "commons-io" % "2.4",
      "com.github.tototoshi" %% "scala-csv" % "1.2.1",
      "st.sparse" %% "sundry" % "0.1.3-SNAPSHOT",
      "org.zeroturnaround" % "zt-zip" % "1.8",
      "com.google.guava" % "guava" % "18.0",
      "com.sksamuel.scrimage" %% "scrimage-core" % "1.4.2",
      "com.sksamuel.scrimage" %% "scrimage-canvas" % "1.4.2",
      "com.sksamuel.scrimage" %% "scrimage-filters" % "1.4.2",
      "com.google.cloud.dataflow" % "google-cloud-dataflow-java-sdk-all" % "0.3.150326"))

  def scalaSettings = Seq(
    scalaVersion := "2.11.6",
    scalacOptions ++= Seq(
      "-optimize",
      "-unchecked",
      "-deprecation",
      "-feature",
      "-language:postfixOps",
      "-language:implicitConversions"))

  def updateOnDependencyChange = Seq(
    watchSources <++= (managedClasspath in Test) map { cp => cp.files })

  lazy val root = {
    val settings =
      Project.defaultSettings ++
        extraResolvers ++
        extraLibraryDependencies ++
        scalaSettings ++
        updateOnDependencyChange ++
        Seq(name := projectName, fork := true)

    Project(id = projectName, base = file("."), settings = settings)
  }
}
