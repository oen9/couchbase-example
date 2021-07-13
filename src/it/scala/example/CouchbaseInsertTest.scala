package example

import com.couchbase.client.java.Cluster
import org.scalatest.BeforeAndAfter
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.testcontainers.couchbase.BucketDefinition
import org.testcontainers.couchbase.CouchbaseContainer

class CouchbaseInsertTest extends AnyFlatSpec with Matchers with BeforeAndAfter with BeforeAndAfterAll {
  val bucketName = "mybucket"

  val bucketDefinition: BucketDefinition = new BucketDefinition(bucketName)
  val container = new CouchbaseContainer("couchbase/server:6.5.2")
    .withBucket(bucketDefinition)

  override def beforeAll(): Unit = container.start()
  override def afterAll(): Unit  = container.stop()

  before {
    val cluster: Cluster = Cluster.connect(
      container.getConnectionString(),
      container.getUsername(),
      container.getPassword()
    )
    cluster.query(s"DELETE FROM `$bucketName` WHERE META().id LIKE '%'")
  }

  def foo(): Unit = {
    val cluster: Cluster = Cluster.connect(
      container.getConnectionString(),
      container.getUsername(),
      container.getPassword()
    )
    val col = cluster.bucket(bucketName).defaultCollection()
    col.insert("foo", "bar")
    val result = col.get("foo").contentAs(classOf[String])
    result shouldBe "bar"
  }

  it should "insert test" in {
    foo()
    "foo" shouldBe "foo"
  }

  it should "insert test2" in {
    foo()
    "bar" shouldBe "bar"
  }
}
