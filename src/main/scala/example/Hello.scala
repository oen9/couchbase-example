package example

import cats.effect.IOApp
import cats.effect.Sync
import cats.effect.{ExitCode, IO}
import cats.implicits._
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Hello extends IOApp {
  implicit def unsafeLogger[F[_]: Sync] = Slf4jLogger.getLogger[F]

  override def run(args: List[String]): IO[ExitCode] = app[IO]()

  def app[F[_]: Sync](): F[ExitCode] =
    for {
      _   <- Logger[F].info("Hello, world!")
      cfg <- AppConfig.load
      _   <- Logger[F].debug(cfg.toString())
    } yield ExitCode.Success
}
