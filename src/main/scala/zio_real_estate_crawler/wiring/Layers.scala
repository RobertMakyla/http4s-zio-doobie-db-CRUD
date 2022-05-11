package zio_real_estate_crawler.wiring

import zio_real_estate_crawler.config.AppConfig
import zio_real_estate_crawler.http.HttpServer
import zio_real_estate_crawler.logging.Logger

object Layers {

  type AppEnv = zio.ZEnv
    with Logger
    with HttpServer
    with AppConfig

//
//  def appLayer  = {
//    Logger.slf4j ++
//      (zio.ZEnv.live >+>
//        (RawConfig.rawConfig >>> AppConfig.fromRawConfig) >+>
//        Port.fromSystemPropOrConfig >+>
//        Endpoints.live >+>
//        HttpServer.live
//        )
//  }
}
