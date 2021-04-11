package example

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class HelloSpec extends AnyFlatSpec with Matchers {
  "Foo" should "foo" in {
    "foo" shouldEqual "foo"
  }
}
