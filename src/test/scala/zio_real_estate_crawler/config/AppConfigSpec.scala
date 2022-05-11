package zio_real_estate_crawler.config

import zio.ZIO
import zio.test.Assertion.equalTo

//object AppConfigSpec extends DefaultRunnableSpec {
//
//  suite("AppConfigSpec")(
//    testM("loading welcome message"){
//      assertM(ZIO.service[AppConfig])(
//        equalTo(
//          AppConfig(
//            welcomeMessage = WelcomeMessage("Welcome to ZIO Real Estate - Web Crawler !"),
//            http = HttpConfig("0.0.0.0", 8080))
//        )
//      )
//    }
//  )
//}
