package zio_real_estate_crawler.wiring

import zhttp.http._
import zhttp.service.server.ServerChannelFactory
import zhttp.service.{EventLoopGroup, Server}
import zio._

object MainApp extends ZIOAppDefault {

  private val PORT = 8090

  private val simpleRoutes: HttpApp[Any, Nothing] = Http.collect[Request] {
    case Method.GET -> !! / "foo" => Response.text("bar")
    case Method.GET -> !! / "bar" => Response.text("foo")
  }

  private val effectualRoutes: HttpApp[Any, Nothing] = Http.collectZIO[Request] {
    case Method.GET -> !! / "random" => Random.nextString(10).map(s => Response.text(s))
    case Method.GET -> !! / "utc" => Clock.currentDateTime.map(s => Response.text(s.toString))
  }

  def run = {
      Server.start(PORT, simpleRoutes ++ effectualRoutes).exitCode
  }
}