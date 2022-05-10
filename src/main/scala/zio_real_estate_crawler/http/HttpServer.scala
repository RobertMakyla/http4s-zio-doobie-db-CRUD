package zio_real_estate_crawler.http

import zhttp.http._
import zhttp.service.server.ServerChannelFactory
import zhttp.service.{EventLoopGroup, Server}
import zio.clock.Clock
import zio.random.Random
import zio.{Has, ZIO, ZLayer, clock, console, random}

import java.time.DateTimeException

trait HttpServer {
  def runForever: ZIO[zio.ZEnv, Throwable, Unit]
}


object HttpServer {

  private case class Live(port: Int, endpoints: Endpoints) extends HttpServer {

    private val server =
      Server.port(port) ++ // Setup port
        Server.paranoidLeakDetection ++ // Paranoid leak detection (affects performance)
        Server.app(endpoints.all) // Setup the Http app

    override def runForever: ZIO[zio.ZEnv, Throwable, Unit] =
      server.make
        .use(start =>
          console.putStrLn(s"Server started on port ${start.port}")
            // Ensures the server doesn't die after printing
            *> ZIO.never,
        )
        .provideCustomLayer(ServerChannelFactory.auto ++ EventLoopGroup.auto(0))
        .as(())

  }

  val live: ZLayer[Has[Port] with Has[Endpoints], Throwable, Has[HttpServer]] = {
    for {
      port <- ZIO.service[Port]
      endpoints <- ZIO.service[Endpoints]
    } yield Live(port.value, endpoints)
  }.toLayer

}
