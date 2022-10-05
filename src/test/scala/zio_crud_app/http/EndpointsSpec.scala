package zio_crud_app.http

import zhttp.http
import zhttp.http.Method.GET
import zhttp.http.{Request, Response, URL}
import zio.test.Assertion.equalTo
import zio.test.{ZIOSpecDefault, assert}
import zio.{ZIO, ZLayer}
import zio_crud_app.config.{AppConfig, HttpConfig, WelcomeMessage}

object EndpointsSpec extends ZIOSpecDefault {

  def spec = {
    suite("EndpointsSpec") {
      test("Welcome message") {
        val request = Request(method = GET, url = URL(http.Path("/")))
        val expectedResponse = Response.text("hello")
        for {
          actualResponse <- ZIO.service[Endpoints].flatMap(_.all.apply(request))
        } yield assert(actualResponse)(equalTo(expectedResponse))
      }
    }.provide(
      ZLayer {
        ZIO.succeed(AppConfig(WelcomeMessage("hello"), HttpConfig("test", 123)))
      } >>> Endpoints.live
    )
  }

}
