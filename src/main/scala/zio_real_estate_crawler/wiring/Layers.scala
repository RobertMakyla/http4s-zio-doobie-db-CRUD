package zio_real_estate_crawler.wiring

import zio.config.ReadError
import zio.{Has, ZLayer}
import zio_real_estate_crawler.config.{AppConfig, RawConfig}
import zio_real_estate_crawler.logging.Logger
import zio_real_estate_crawler.port.Port

object Layers {

  type AppEnv = zio.ZEnv
    with Has[Logger]
    with Has[Port]
  //  with Has[AppConfig]

  def appLayer: ZLayer[Any, Throwable, AppEnv] =
    zio.ZEnv.live ++
      Port.fromSystemPropertyOr8080 ++
      Logger.slf4j //++
//      (RawConfig.rawConfig >>> AppConfig.fromRawConfig)
}
