import org.scalatest.funsuite.AnyFunSuite;
import org.scalatest.matchers.should.Matchers;

class MaxFromScratchTest extends AnyFunSuite with Matchers {

  test("Test order list") {
    learning.MaxFromScratch.mainMax(10, 0, 1) should be (10);
  }
}
