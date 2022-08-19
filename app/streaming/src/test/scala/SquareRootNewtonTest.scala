import scala.math.sqrt
import org.scalatest.funsuite.AnyFunSuite;
import org.scalatest.matchers.should.Matchers

class SquareRootNewtonTest extends AnyFunSuite with Matchers {

  test("Test simplest case") {

    val checkValue:Double = 4D;
    learning.SquareRootNewton.sqrt(checkValue) should be (sqrt(checkValue) +- 0.1);
  }
}
