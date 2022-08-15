import org.scalatest.funsuite.AnyFunSuite;
import org.scalatest.matchers.should.Matchers;

class FlattenArrayTest extends AnyFunSuite with Matchers {

  test("test flat no nested list") {
    learning.FlattenArray.flatten(List(1, 2, 3)
    ) should be (List(1, 2, 3))
  }

  test("flattens array with just integers present") {
    learning.FlattenArray.flatten(List(1, List(2, 3, 4, 5, 6, 7), 8)) should be(
      List(1, 2, 3, 4, 5, 6, 7, 8))
  }
  test("5 level nesting") {
    learning.FlattenArray.flatten(List(0,
      2,
      List(List(2, 3), 8, 100, 4, List(List(List(50)))),
      -2)) should be(
      List(0, 2, 2, 3, 8, 100, 4, 50, -2))
  }
  test("6 level nesting") {
    learning.FlattenArray.flatten(List(
      1,
      List(2, List(List(3)), List(4, List(List(5))), 6, 7),
      8)) should be(List(1, 2, 3, 4, 5, 6, 7, 8))
  }
  test("6 level nest list with null values") {
    learning.FlattenArray.flatten(
      List(0,
        2,
        List(List(2, 3), 8, List(List(100)), null, List(List(null))),
        -2)) should be(List(0, 2, 2, 3, 8, 100, -2))
  }
  test("all values in nested list are null") {
    learning.FlattenArray.flatten(
      List(null,
        List(List(List(null))),
        null,
        null,
        List(List(null, null), null),
        null)) should be(List())
  }
}
