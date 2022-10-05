package zio_crud_app.http

import zio.System
import zio.{ZIO, ZLayer}
import zio_crud_app.config.AppConfig

case class Port(value: Int)

object Port {

  private val key = "http.port"

  val fromSystemPropOrConfig: ZLayer[AppConfig with System, Nothing, Port] = ZLayer {
    ZIO.service[System]
      .flatMap(sys => sys.property(key))
      .flatMap(optStr => ZIO.attempt(Port(optStr.getOrElse(throw new Exception(s"Missing system property $key")).toInt)))
      .orElse {
        ZIO.service[AppConfig].map(conf => Port(conf.http.port))
      }
  }

}
