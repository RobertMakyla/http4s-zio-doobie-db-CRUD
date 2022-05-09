package zio_real_estate_crawler.http

import zio.system.System
import zio.{Has, ZIO, ZLayer}
import zio_real_estate_crawler.config.AppConfig

case class Port(value: Int)

object Port {

  private val key = "http.port"

  val fromSystemPropOrConfig: ZLayer[Has[AppConfig] with Has[System.Service], Nothing, Has[Port]] = {
    ZIO.service[System.Service]
      .flatMap(sys => sys.property(key))
      .flatMap(optStr => ZIO.effect(Port(optStr.getOrElse(throw new Exception(s"Missing system property $key")).toInt)))
      .orElse {
        ZIO.service[AppConfig].map(conf => Port(conf.http.port))
      }
  }.toLayer

}
