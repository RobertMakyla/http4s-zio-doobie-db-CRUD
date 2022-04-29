name := "zio_real_estate_crawler"

version := "1.0"

scalaVersion := "2.13.8"

libraryDependencies ++= {
  val zioVersion = "2.0.0-RC5"
  val zhttpVersion = "2.0.0-RC5" // https://repo1.maven.org/maven2/io/d11/

  Seq(
    "dev.zio" %% "zio" % zioVersion,
    "dev.zio" %% "zio-streams" % zioVersion,
    "io.d11" %% "zhttp" % zhttpVersion,
    "io.d11" %% "zhttp-test" % zhttpVersion % Test
  )
}