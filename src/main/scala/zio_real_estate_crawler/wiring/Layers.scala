package zio_real_estate_crawler.wiring

import zio.{Has, ZLayer}
import zio_real_estate_crawler.config.{AppConfig, RawConfig}
import zio_real_estate_crawler.http.{HttpServer, Port}
import zio_real_estate_crawler.logging.Logger

object Layers {

  type AppEnv = zio.ZEnv
    with Has[Logger]
    with Has[HttpServer]
    with Has[AppConfig]

  private val appConfigLayer: ZLayer[Any, Throwable, Has[AppConfig]] = RawConfig.rawConfig >>> AppConfig.fromRawConfig

  def appLayer: ZLayer[Any, Throwable, AppEnv] = {
    Logger.slf4j ++
      (appConfigLayer >+> zio.ZEnv.live >+> Port.fromSystemPropOrConfig >+> HttpServer.live)
  }
}
