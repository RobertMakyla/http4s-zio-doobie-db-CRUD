package zio_real_estate_crawler.logging

import org.slf4j.LoggerFactory
import zio.clock.Clock
import zio.{Has, UIO, ULayer, URIO, ZIO}

/**
 * service definition
 */
trait Logger {

  def trace(msg: => String): UIO[Unit]
  def debug(msg: => String): UIO[Unit]
  def info(msg: => String): UIO[Unit]
  def warn(msg: => String): UIO[Unit]
  def error(msg: => String): UIO[Unit]

  def infoWithTimestamp(msg: => String): URIO[Has[Clock], Unit]
}

object Logger {

  private def named(loggerName: String): ULayer[Has[Logger]] =
    ZIO.effectTotal(
      Slf4JLogger(
        LoggerFactory.getLogger(loggerName)
      )
    ).toLayer

  val slf4j: ULayer[Has[Logger]] = named("app")
  val httpLogger: ULayer[Has[Logger]] = named("zio_real_estate_crawler.http")

  /**
   * service implementation
   */
  private case class Slf4JLogger(logger: org.slf4j.Logger) extends Logger {
    override def trace(msg: => String): UIO[Unit] = ZIO.effectTotal(logger.trace(msg))
    override def debug(msg: => String): UIO[Unit] = ZIO.effectTotal(logger.debug(msg))
    override def info(msg: => String): UIO[Unit] = ZIO.effectTotal(logger.info(msg))
    override def warn(msg: => String): UIO[Unit] = ZIO.effectTotal(logger.warn(msg))
    override def error(msg: => String): UIO[Unit] = ZIO.effectTotal(logger.error(msg))

    override def infoWithTimestamp(msg: => String): URIO[Has[Clock], Unit] = ZIO.access[Has[Clock]]( // accessing environment
      clock =>
        logger.info(clock.get.get.currentDateTime.toString + " - " + msg)
    )
  }
}