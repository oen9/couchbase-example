package example

import cats.effect.Async
import cats.effect.kernel.Resource
import cats.implicits._
import com.couchbase.client.java.Cluster
import com.couchbase.client.java.json.JsonObject
import com.couchbase.client.java.ReactiveCollection
import example.commons.Implicits._
import example.commons.Logging
import java.time.ZonedDateTime
import org.typelevel.log4cats.Logger

object DirtyCouchbaseExample extends Logging {

  def exec[F[_]: Async](cfg: CouchbaseConfig): F[Unit] = makeCluster(cfg).use { cluster =>
    for {
      _    <- Logger[F].info("Starting couchbase app")
      coll <- getCollection(cluster, cfg)
      _    <- insertFooDate(coll)
      _    <- insertCaseClass(coll)
      _    <- lockTest(coll)
      _    <- Logger[F].info("Couchbase app finished")
    } yield ()
  }

  def makeCluster[F[_]: Async](cfg: CouchbaseConfig): Resource[F, Cluster] =
    Resource.make {
      Async[F].blocking(Cluster.connect(cfg.host, cfg.username, cfg.password))
    }(cluster => Async[F].blocking(cluster.disconnect()))

  def getCollection[F[_]: Async](cluster: Cluster, cfg: CouchbaseConfig): F[ReactiveCollection] = Async[F].delay {
    val bucket         = cluster.bucket(cfg.bucket)
    val reactiveBucket = bucket.reactive()
    reactiveBucket.defaultCollection()
  }

  def insertFooDate[F[_]: Async](coll: ReactiveCollection): F[Unit] =
    for {
      insertResult <- coll.upsert("foo", "bar" + ZonedDateTime.now()).toAsyncF
      _            <- Logger[F].debug(insertResult.toString())
      getResult    <- coll.get("foo").toAsyncF
      _            <- Logger[F].debug(getResult.toString())
      _            <- Logger[F].info(getResult.contentAs(classOf[String]))
    } yield ()

  def insertCaseClass[F[_]: Async](coll: ReactiveCollection): F[Unit] = {
    import io.circe.generic.auto._
    import io.circe.parser._
    import io.circe.syntax._

    case class MyFooClass(id: Long, value: String, number: Long)
    val myFooClass = MyFooClass(1, "foo", 3)
    val id         = s"myclass::${myFooClass.id}"

    val json = JsonObject.fromJson(myFooClass.asJson.noSpaces)
    for {
      insertResult <- coll.upsert(id, json).toAsyncF
      _            <- Logger[F].debug(insertResult.toString())
      getResult    <- coll.get(id).toAsyncF
      _            <- Logger[F].debug(getResult.toString())

      resultAsJson = getResult.contentAsObject().toString()
      parsed <- Async[F].fromEither(decode[MyFooClass](resultAsJson))

      _ <- Logger[F].info(parsed.toString())
    } yield ()
  }

  def lockTest[F[_]: Async](coll: ReactiveCollection): F[Unit] =
    for {
      _ <- Logger[F].debug("Lock test: maybe soon ...")
    } yield ()
}
