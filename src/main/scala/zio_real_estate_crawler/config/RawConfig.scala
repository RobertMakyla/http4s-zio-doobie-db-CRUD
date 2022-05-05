package zio_real_estate_crawler.config

import com.typesafe.config.{Config, ConfigFactory}
import zio.{Has, ZIO, ZLayer}

case class RawConfig(config: Config)

object RawConfig {

  val rawConfig: ZLayer[Any, Throwable, Has[RawConfig]] =
    ZIO.effect(
      RawConfig(ConfigFactory.load())
    ).toLayer

}
