package zio_real_estate_crawler.http

import zio.test.TestConsole
import zio._
import zio.test._
import zio.test.Assertion._
import Port._
import zio.test.TestSystem.Data
import zio_real_estate_crawler.config.{AppConfig, HttpConfig, WelcomeMessage}
import zio_real_estate_crawler.http.PortSpec.test

import java.io.IOException
object PortSpec extends ZIOSpecDefault {

  def spec = suite("PortSpec")(

    test("read port from the system property") {
      for {
        port <- ZIO.service[Port]
      } yield assertTrue(port.value == 123)
    }.provide(
      ZLayer(ZIO.succeed(AppConfig(WelcomeMessage("hi test"), HttpConfig("testhost", 666)))),
      TestSystem.live(Data(Map("http.port" -> "123"), Map(), "\n")),
      Port.fromSystemPropOrConfig
    )

  )
}