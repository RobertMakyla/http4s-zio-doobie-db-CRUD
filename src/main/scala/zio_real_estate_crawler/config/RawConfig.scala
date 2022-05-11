package zio_real_estate_crawler.config

import com.typesafe.config.{Config, ConfigFactory}
import zio.{ZIO, ZLayer}

case class RawConfig(config: Config)

object RawConfig {

  val rawConfig: ZLayer[Any, Throwable, RawConfig] =
    ZLayer(
      ZIO.attempt(
        RawConfig(ConfigFactory.load())
      )
    )

}
