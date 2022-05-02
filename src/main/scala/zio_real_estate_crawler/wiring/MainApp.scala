package zio_real_estate_crawler.wiring

import zhttp.http._
import zhttp.service.server.ServerChannelFactory
import zhttp.service.{EventLoopGroup, Server}
import zio._
import zio.clock.Clock
import zio.random.Random

import java.time.DateTimeException
import scala.util.Try

object MainApp extends App {

  private val PORT = 6666

  private val simpleRoutes: HttpApp[Any, Nothing] = Http.collect[Request] {
    case Method.GET -> !! / "foo" => Response.text("bar")
    case Method.GET -> !! / "bar" => Response.text("foo")
  }

  private val effectualRoutes: HttpApp[Random with Clock, DateTimeException] = Http.collectZIO[Request] {
    case Method.GET -> !! / "random" => random.nextString(10).map(s => Response.text(s))
    case Method.GET -> !! / "utc" => clock.currentDateTime.map(s => Response.text(s.toString))
  }

  private val server =
    Server.port(PORT) ++              // Setup port
      Server.paranoidLeakDetection ++ // Paranoid leak detection (affects performance)
      Server.app(simpleRoutes ++ effectualRoutes)       // Setup the Http app

  override def run(args: List[String]) = {

    // Create a new server
    server.make
      .use(start =>
        console.putStrLn(s"Server started on port ${start.port}")
          // Ensures the server doesn't die after printing
          *> ZIO.never,
      )
      .provideCustomLayer(ServerChannelFactory.auto ++ EventLoopGroup.auto(0))
      .exitCode
  }
}