name := "zio-real-estate-crawler"

version := "1.0"

scalaVersion := "2.13.8"

libraryDependencies ++= {
  val zioVersion = "2.0.0-RC5"
  val zioConfigVersion = "2.0.4"   // https://repo1.maven.org/maven2/dev/zio/zio-config-typesafe_2.13/
  val zhttpVersion = "2.0.0-RC7" // https://repo1.maven.org/maven2/io/d11/

  Seq(
    "dev.zio" %% "zio" % zioVersion,
//    "dev.zio" %% "zio-test" % zioVersion,
    "dev.zio" %% "zio-config" % zioConfigVersion,
    "dev.zio" %% "zio-config-typesafe" % zioConfigVersion,
    "io.d11" %% "zhttp" % zhttpVersion,
 //   "io.d11" %% "zhttp-test" % zhttpVersion % Test,
    "com.typesafe" % "config" % "1.4.2",
    "org.slf4j" % "slf4j-api" % "1.7.36"//,
//    "org.slf4j" % "slf4j-simple" % "1.7.36"
  )
}
enablePlugins(JavaAppPackaging)