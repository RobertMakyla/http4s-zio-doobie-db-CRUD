logLevel := Level.Warn

/*
 * sbt-revolver is a plugin for SBT enabling a super-fast development turnaround
 * for your Scala applications with commands:
 *    sbt ~re-start  -watches code change, re-compiles and restarts
 *    sbt re-stop
 *    sbt re-status
 */
addSbtPlugin("io.spray" % "sbt-revolver" % "0.8.0")

/* sbt-assembly creates a fat JAR of your project with all of its dependencies */
// addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")

/* SBT native packager lets you build application packages in native formats.
 * eg: zip, tar, gz.
 */
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.3")