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

  private case class Live(port: Int) extends HttpServer {

    private val simpleRoutes: HttpApp[Any, Nothing] = Http.collect[Request] {
      case Method.GET -> !! / "foo" => Response.text("bar")
      case Method.GET -> !! / "bar" => Response.text("foo")
    }

    private val effectualRoutes: HttpApp[Random with Clock, DateTimeException] = Http.collectZIO[Request] {
      case Method.GET -> !! / "random" => random.nextString(10).map(s => Response.text(s))
      case Method.GET -> !! / "utc" => clock.currentDateTime.map(s => Response.text(s.toString))
    }

    private val server =
      Server.port(port) ++ // Setup port
        Server.paranoidLeakDetection ++ // Paranoid leak detection (affects performance)
        Server.app(simpleRoutes ++ effectualRoutes) // Setup the Http app

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

  val live: ZLayer[Has[Port], Throwable, Has[HttpServer]] = {
    for {
      port <- ZIO.service[Port]
    } yield Live(port.value)
  }.toLayer

}
