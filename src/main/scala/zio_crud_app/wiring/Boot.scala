package zio_crud_app.wiring

import zio._
import zio.Clock
import zio_crud_app.config.{AppConfig, RawConfig}
import zio_crud_app.http.{Endpoints, HttpServer, Port}
import zio_crud_app.logging.Logger
import zio_crud_app.wiring.Layers._


object Boot extends ZIOAppDefault {

  private def runInit: ZIO[Clock with Logger, Nothing, Unit] =
    ZIO.service[Logger]
      .flatMap(logger =>
        logger.infoWithTimestamp("Starting the app")
      )

  private def runServer: ZIO[zio.ZEnv with HttpServer, Throwable, Unit] = {
    ZIO.service[HttpServer]
      .flatMap(server =>
        server.runForever
      )
  }

  private def runApp: ZIO[AppEnv, Throwable, Unit] = runInit *> runServer

  /*
     The 'provide()' builds dependency graph automatically . No need to use >>> and >+>  :

      (RawConfig.rawConfig >>> AppConfig.fromRawConfig ) >+>  // config
      Logger.slf4j >+> // config & Logger
      ZEnv.live >+>    // config & Logger & ZEnv.live
      port  >+>        // config & Logger & ZEnv.live & port
      endpoints >+>    // config & Logger & ZEnv.live & port & endpoints
      server           // config & Logger & ZEnv.live & port & endpoints & server
   */
  override def run: ZIO[Any, Throwable, Unit] = runApp.provide(
    RawConfig.rawConfig,
    AppConfig.fromRawConfig,
    Logger.slf4j,
    zio.ZEnv.live,
    Port.fromSystemPropOrConfig,
    Endpoints.live,
    HttpServer.live
  )
}