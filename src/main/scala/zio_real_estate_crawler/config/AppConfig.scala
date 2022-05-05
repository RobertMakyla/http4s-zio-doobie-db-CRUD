package zio_real_estate_crawler.config

import zio.{Has, ZIO, ZLayer}
import zio.config._
import ConfigDescriptor._
import zio.config.typesafe.TypesafeConfig

case class AppConfig(welcomeMessage: WelcomeMessage, http: HttpConfig)

case class WelcomeMessage(value: String)

case class HttpConfig(host: String, port: Int)

object AppConfig {

  val welcome: ConfigDescriptor[WelcomeMessage] =
    string("welcome") (WelcomeMessage.apply, WelcomeMessage.unapply)

  val http: ConfigDescriptor[HttpConfig] =
    (string("host") |@| int("port")) (HttpConfig.apply, HttpConfig.unapply)

  val appConfig: ConfigDescriptor[AppConfig] =
    (welcome |@| http) (AppConfig.apply, AppConfig.unapply)

  val fromRawConfig: ZLayer[Has[RawConfig], ReadError[String], Has[AppConfig]] =
    TypesafeConfig.fromTypesafeConfigM(
      ZIO.service[RawConfig].map(_.config), appConfig
    )
}

