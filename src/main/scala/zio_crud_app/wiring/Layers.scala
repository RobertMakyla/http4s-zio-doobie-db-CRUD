package zio_crud_app.wiring

import zio_crud_app.config.AppConfig
import zio_crud_app.http.HttpServer
import zio_crud_app.logging.Logger

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
