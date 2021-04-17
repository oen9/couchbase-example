package example.commons

import cats.effect.kernel.Async
import reactor.core.publisher.Mono

object Implicits {
  implicit class MonoToAsyncF[F[_]: Async, A](value: Mono[A]) {
    def toAsyncF: F[A] = Async[F].fromCompletableFuture(Async[F].delay(value.toFuture()))
  }
}
