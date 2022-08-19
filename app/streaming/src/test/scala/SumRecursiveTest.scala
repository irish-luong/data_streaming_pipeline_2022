
import org.scalatest.funsuite.AnyFunSuite;
import org.scalatest.matchers.should.Matchers;
import scala.collection.immutable

class SumRecursiveTest extends AnyFunSuite with Matchers {

  test("Test the simplest case") {
      val testList = immutable.List(1,2,3,4,5)
      learning.SumRecursive.sumA(testList) should be (testList.sum);
      learning.SumRecursive.sumB(testList) should be (testList.sum);
  }
}
