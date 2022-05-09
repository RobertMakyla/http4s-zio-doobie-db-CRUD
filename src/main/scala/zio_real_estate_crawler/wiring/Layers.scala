package zio_real_estate_crawler.wiring

import zio.config.ReadError
import zio.{Has, ZLayer}
import zio_real_estate_crawler.config.{AppConfig, RawConfig}
import zio_real_estate_crawler.http.{HttpServer, Port}
import zio_real_estate_crawler.logging.Logger

object Layers {

  type AppEnv = zio.ZEnv
    with Has[Logger]
    with Has[HttpServer]
  //  with Has[AppConfig]

  def appLayer: ZLayer[Any, Throwable, AppEnv] =
    zio.ZEnv.live ++
      Logger.slf4j ++
      // (RawConfig.rawConfig >>> AppConfig.fromRawConfig)++
      (Port.fromSystemPropertyOrElse8080 >>> HttpServer.live)
}
