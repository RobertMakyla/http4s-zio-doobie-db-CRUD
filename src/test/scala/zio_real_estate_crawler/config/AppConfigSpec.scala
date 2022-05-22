package zio_real_estate_crawler.config

import com.typesafe.config.ConfigFactory
import zio.{ZIO, _}
import zio.config.PropertyTreePath.Step
import zio.config.ReadError.ConversionError
import zio.test.Assertion._
import zio.test.{ZIOSpecDefault, _}

object AppConfigSpec extends ZIOSpecDefault {

  def spec = {
    suite("AppConfigSpec")(
      test("is loading correct config") {
        assertM(ZIO.service[AppConfig])(
          equalTo(
            AppConfig(welcomeMessage = WelcomeMessage("Test welcome message"), http = HttpConfig("testhost", 1234))
          )
        )
      }.provide(testConfig.orDie)
    )
  }

  val testConfig: ZLayer[Any, Throwable, AppConfig] = configFromResource("test.conf")

  def configFromResource(path: String) = ZLayer {
    ZIO.attempt(ConfigFactory.load(path)).map(RawConfig(_))
  } >>> AppConfig.fromRawConfig

}
