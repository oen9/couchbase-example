package example

import cats.effect.Async
import cats.effect.IOApp
import cats.effect.std.Console
import cats.effect.{ExitCode, IO}
import cats.implicits._
import example.commons.Logging
import org.typelevel.log4cats.Logger

object Hello extends IOApp with Logging {

  override def run(args: List[String]): IO[ExitCode] = app[IO]()

  def app[F[_]: Async: Console](): F[ExitCode] =
    for {
      _   <- Logger[F].info("Hello, world!")
      cfg <- AppConfig.load
      _   <- Logger[F].debug(s"$cfg")
      _   <- DirtyCouchbaseExample.exec(cfg.couchbase)
    } yield ExitCode.Success
}
