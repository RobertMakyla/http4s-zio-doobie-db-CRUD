package zio_real_estate_crawler.wiring

import zio._
import zio.clock.Clock
import zio_real_estate_crawler.http.HttpServer
import zio_real_estate_crawler.logging.Logger
import zio_real_estate_crawler.wiring.Layers._

object Boot extends App {

  private def runInit: ZIO[Has[Clock.Service] with Has[Logger], Nothing, Unit] =
    ZIO.service[Logger]
      .flatMap(logger =>
        logger.infoWithTimestamp("Starting the app")
      )

  private def runServer: ZIO[zio.ZEnv with Has[HttpServer], Throwable, Unit] = {
    ZIO.service[HttpServer]
      .flatMap(server =>
        server.runForever
      )
  }

  private def runApp: ZIO[AppEnv, Throwable, Unit] = runInit *> runServer

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = runApp.provideLayer(Layers.appLayer).exitCode
}