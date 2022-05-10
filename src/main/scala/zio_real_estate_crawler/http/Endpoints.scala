package zio_real_estate_crawler.http

import zhttp.http._
import zio.{Has, ULayer, ZIO, ZLayer}
import zio_real_estate_crawler.config.AppConfig

trait Endpoints {
  val all: HttpApp[Any, Throwable]
}

object Endpoints {

  private case class Live(config: AppConfig) extends Endpoints {

    private val simpleRoutes: HttpApp[Any, Nothing] = Http.collect[Request] {
      case Method.GET -> !!  => Response.text(config.welcomeMessage.value)
      case Method.GET -> !! / "foo" => Response.text("bar")
      case Method.GET -> !! / "bar" => Response.text("foo")
    }

//    private val effectualRoutes: HttpApp[Random with Clock, DateTimeException] = Http.collectZIO[Request] {
//      case Method.GET -> !! / "random" => random.nextString(10).map(s => Response.text(s))
//      case Method.GET -> !! / "utc" => clock.currentDateTime.map(s => Response.text(s.toString))
//    }

    val all = simpleRoutes // ++ effectualRoutes
  }

  val live: ZLayer[Has[AppConfig], Nothing, Has[Endpoints]] = {
    for{
      config <- ZIO.service[AppConfig]
    } yield Live(config)
  }.toLayer

}
