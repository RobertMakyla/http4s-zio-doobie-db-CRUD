package zio_crud_app

import zio.test.{TestConsole, TestRandom, TestSystem, ZIOSpecDefault, assertTrue}
import zio.{Console, Random, System}

object HowThisWorksSpec extends ZIOSpecDefault {

  def spec = {

    suite("built-in services")(
      test("TestConsole") {
        for {
          _ <- Console.printLine("Hello, World!")
          res <- TestConsole.output
        } yield assertTrue(res == Vector("Hello, World!\n"))
      },
      test("TestSystem") {
        for {
          _ <- TestSystem.putProperty("k", "abc")
          res <- System.property("k")
        } yield assertTrue(res == Some("abc"))
      },
      test("TestRandom - use feedInts for concrete values, setSeed for stable values") {
        for {
          _ <- TestRandom.feedInts(3, 9, 2)
          r1 <- Random.nextInt
          r2 <- Random.nextInt
          r3 <- Random.nextInt
        } yield assertTrue(List(3, 9, 2) == List(r1, r2, r3))
      }

    )
  }
}
