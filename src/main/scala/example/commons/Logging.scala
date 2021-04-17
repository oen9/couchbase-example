package example.commons

import cats.effect.kernel.Sync
import org.typelevel.log4cats.slf4j.Slf4jLogger

trait Logging {
  implicit def unsafeLogger[F[_]: Sync] = Slf4jLogger.getLoggerFromClass[F](getClass())
}
