package zio_real_estate_crawler.config

import com.typesafe.config.ConfigFactory
import zio.{ZIO, _}
import zio.config.PropertyTreePath.Step
import zio.config.ReadError.ConversionError
import zio.test.Assertion._
import zio.test.{ZIOSpecDefault, _}

object AppConfigSpec extends ZIOSpecDefault {

  val expected: ConversionError[String] =
    ConversionError(List(Step.Key("changeme")), "Predicate isEmpty() did not fail.", Set.empty)

  def spec = {
    suite("AppConfigSpec")(
      test("is loading correct config") {
        assertM(ZIO.service[AppConfig])(
          equalTo(
            AppConfig(welcomeMessage = WelcomeMessage("Test welcome message"), http = HttpConfig("testhost", 1234))
          )
        )
      }.provide(correctTestConfig.orDie)
    )
  }

  val correctTestConfig: ZLayer[Any, Throwable, AppConfig] = configFromResource("test.conf")

  def configFromResource(path: String) = ZLayer {
    ZIO.attempt(ConfigFactory.load(path)).map(RawConfig(_))
  } >>> AppConfig.fromRawConfig

}
