package zio_crud_app.config

import zio.{IO, ZIO, ZLayer}
import zio.config._
import ConfigDescriptor._
import zio.config.typesafe.{TypesafeConfig, TypesafeConfigSource}

case class AppConfig(welcomeMessage: WelcomeMessage, http: HttpConfig)

case class WelcomeMessage(value: String)

case class HttpConfig(host: String, port: Int)

object AppConfig {

  val welcome: ConfigDescriptor[WelcomeMessage] =
    string("welcome")(WelcomeMessage.apply, WelcomeMessage.unapply)

  val http: ConfigDescriptor[HttpConfig] =
    string("host").zip(int("port")).to[HttpConfig]

  val appConfig: ConfigDescriptor[AppConfig] =
    welcome.zip(nested("http")(http)).to[AppConfig]

  val fromRawConfig: ZLayer[RawConfig, ReadError[String], AppConfig] = ZLayer {
    for {
      rawConfig <- ZIO.service[RawConfig]
      appConfig <- read(appConfig.from(TypesafeConfigSource.fromTypesafeConfig(ZIO.succeed(rawConfig.config))))
    } yield appConfig
  }

}

