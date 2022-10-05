package zio_crud_app.config

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
