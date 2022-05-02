name := "zio-real-estate-crawler"

version := "1.0"

scalaVersion := "2.13.8"

libraryDependencies ++= {
  val zioVersion = "1.0.11"
  val zhttpVersion = "1.0.0.0-RC27" // https://repo1.maven.org/maven2/io/d11/

  Seq(
    "dev.zio" %% "zio" % zioVersion,
    "dev.zio" %% "zio-streams" % zioVersion,
    "io.d11" %% "zhttp" % zhttpVersion,
    "io.d11" %% "zhttp-test" % zhttpVersion % Test
  )
}
enablePlugins(JavaAppPackaging)