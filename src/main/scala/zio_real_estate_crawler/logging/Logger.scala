package zio_real_estate_crawler.logging

import org.slf4j.LoggerFactory
import zio.{Clock, UIO, ULayer, URIO, ZIO, ZLayer}

import java.time.LocalDateTime

/**
 * service definition
 */
trait Logger {

  def trace(msg: => String): UIO[Unit]
  def debug(msg: => String): UIO[Unit]
  def info(msg: => String): UIO[Unit]
  def warn(msg: => String): UIO[Unit]
  def error(msg: => String): UIO[Unit]

  def infoWithTimestamp(msg: => String): URIO[Clock, Unit]
}

object Logger {

  private def named(loggerName: String): ULayer[Logger] = ZLayer {
    ZIO.succeed(
      Slf4JLogger(
        LoggerFactory.getLogger(loggerName)
      )
    )
  }

  val slf4j: ULayer[Logger] = named("app")
  val httpLogger: ULayer[Logger] = named("zio_real_estate_crawler.http")

  /**
   * service implementation
   */
  private case class Slf4JLogger(logger: org.slf4j.Logger) extends Logger {
    override def trace(msg: => String): UIO[Unit] = ZIO.succeed(logger.trace(msg))
    override def debug(msg: => String): UIO[Unit] = ZIO.succeed(logger.debug(msg))
    override def info(msg: => String): UIO[Unit] = ZIO.succeed(logger.info(msg))
    override def warn(msg: => String): UIO[Unit] = ZIO.succeed(logger.warn(msg))
    override def error(msg: => String): UIO[Unit] = ZIO.succeed(logger.error(msg))

    override def infoWithTimestamp(msg: => String): URIO[Clock, Unit] = { // accessing environment
      ZIO.service[Clock].flatMap(
        _.localDateTime.flatMap(time =>
          ZIO.succeed(logger.info(time.toString + " - " + msg))
        )
      )
    }

  }
}