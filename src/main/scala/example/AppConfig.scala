package example

import cats.effect.Sync
import pureconfig.ConfigSource
import pureconfig.generic.auto._

case class AppConfig(couchbase: CouchbaseConfig)
case class CouchbaseConfig(host: String, username: String, password: String, bucket: String)

object AppConfig {
  def load[F[_]: Sync](implicit F: Sync[F]): F[AppConfig] = F.delay(ConfigSource.default.loadOrThrow[AppConfig])
}
