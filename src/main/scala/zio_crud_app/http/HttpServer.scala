package zio_crud_app.http

import zhttp.http.HttpApp
import zhttp.service.Server
import zio.{ZIO, ZLayer}

trait HttpServer {
  def runForever: ZIO[zio.ZEnv, Throwable, Nothing]
}

object HttpServer {

  private case class Live(port: Int, endpoints: HttpApp[Any, Throwable]) extends HttpServer {

    override def runForever: ZIO[Any, Throwable, Nothing] =
      Server.start(port, endpoints)
  }

  val live: ZLayer[Port with Endpoints, Throwable, HttpServer] = ZLayer {
    for {
      port <- ZIO.service[Port]
      endpoints <- ZIO.service[Endpoints]
    } yield Live(port.value, endpoints.all)
  }

}
