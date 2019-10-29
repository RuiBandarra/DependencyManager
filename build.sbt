name := """DependencyResolverWebApp"""
organization := "com.example"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.0"

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test,
  "org.eclipse.aether" % "aether-api" % "1.1.0",
  "org.eclipse.aether" % "aether-spi" % "1.1.0",
  "org.eclipse.aether" % "aether-util" % "1.1.0",
  "org.eclipse.aether" % "aether-impl" % "1.1.0",
  "org.eclipse.aether" % "aether-connector-basic" % "1.1.0",
  "org.eclipse.aether" % "aether-transport-classpath" % "1.1.0",
  "org.eclipse.aether" % "aether-transport-file" % "1.1.0",
  "org.eclipse.aether" % "aether-transport-http" % "1.1.0",
  "org.eclipse.aether" % "aether-transport-wagon" % "1.1.0",
  "org.apache.maven" % "maven-resolver-provider" % "3.6.0",
  "org.apache.maven" % "maven-settings" % "3.6.0",
  "org.apache.maven" % "maven-settings-builder" % "3.6.0",
  "org.codehaus.plexus" % "plexus-logging" % "1.0.4" pomOnly(),
  "org.codehaus.plexus" % "plexus-container-default" % "1.7.1",
  "commons-validator" % "commons-validator" % "1.6"
)
