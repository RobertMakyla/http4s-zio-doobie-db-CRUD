package zio_real_estate_crawler.wiring

import zhttp.http._
import zhttp.service.server.ServerChannelFactory
import zhttp.service.{EventLoopGroup, Server}
import zio._
import zio.clock.Clock
import zio.random.Random
import zio_real_estate_crawler.config.AppConfig
import zio_real_estate_crawler.logging.Logger
import zio_real_estate_crawler.port.Port
import zio_real_estate_crawler.wiring.Layers._

import java.time.DateTimeException
import scala.util.Try

object Boot extends App {

  private val simpleRoutes: HttpApp[Any, Nothing] = Http.collect[Request] {
    case Method.GET -> !! / "foo" => Response.text("bar")
    case Method.GET -> !! / "bar" => Response.text("foo")
  }

  private val effectualRoutes: HttpApp[Random with Clock, DateTimeException] = Http.collectZIO[Request] {
    case Method.GET -> !! / "random" => random.nextString(10).map(s => Response.text(s))
    case Method.GET -> !! / "utc" => clock.currentDateTime.map(s => Response.text(s.toString))
  }

  private val server = (port: Int) =>
    Server.port(port) ++              // Setup port
      Server.paranoidLeakDetection ++ // Paranoid leak detection (affects performance)
      Server.app(simpleRoutes ++ effectualRoutes)       // Setup the Http app

  def runInit: ZIO[Has[Clock.Service] with Has[Logger], Nothing, Unit] = ZIO.service[Logger].flatMap(_.infoWithTimestamp("Starting the app"))

  def runServer: ZIO[zio.ZEnv with Has[Port], Throwable, Unit] = {
    ZIO.service[Port].flatMap(port =>
      server(port.value).make
        .use(start =>
          console.putStrLn(s"Server started on port ${start.port}")
            // Ensures the server doesn't die after printing
            *> ZIO.never,
        )
        .provideCustomLayer(ServerChannelFactory.auto ++ EventLoopGroup.auto(0))
        .as(())
    )

  }

  def runApp: ZIO[AppEnv, Throwable, Unit] = runInit *> runServer

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = runApp.provideLayer(Layers.appLayer).exitCode
}