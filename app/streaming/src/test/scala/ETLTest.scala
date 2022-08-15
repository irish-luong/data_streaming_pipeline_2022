import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import scala.collection.immutable

class ETLTest extends AnyFunSuite with Matchers {

  test("test single letter") {
    learning.ETL.transform(
      immutable.Map(1 -> Seq("A"))
    ) should be(immutable.Map("a" -> 1))
  }

  test("single score with multiple letters") {
    learning.ETL.transform(
      immutable.Map(1 -> Seq("A", "B", "C", "D"))
    ) should be(immutable.Map("a" -> 1, "b" -> 1, "c" -> 1, "d" -> 1))
  }

  test("multiple score with multiple letters") {
    learning.ETL.transform(
      immutable.Map(1 -> Seq("A", "B"), 2 -> Seq("C", "D"))
    ) should be(immutable.Map("a" -> 1, "b" -> 1, "c" -> 2, "d" -> 2))
  }

  test("mutiple score with repeated value") {
    learning.ETL.transform(
      immutable.Map(1 -> Seq("A"), 2 -> Seq("A", "C"))
    ) should be(immutable.Map("a" -> 2, "c" -> 2))
  }

  test("""multiple scores with differing numbers of letters""") {
    learning.ETL.transform(Map(1 -> Seq("A", "E", "I", "O", "U", "L", "N", "R", "S", "T"),
      2 -> Seq("D", "G"), 3 -> Seq("B", "C", "M", "P"), 4 -> Seq("F", "H", "V", "W", "Y"),
      5 -> Seq("K"), 8 -> Seq("J", "X"), 10 -> Seq("Q", "Z"))) should be(Map("a" -> 1,
      "b" -> 3, "c" -> 3, "d" -> 2, "e" -> 1, "f" -> 4, "g" -> 2, "h" -> 4,
      "i" -> 1, "j" -> 8, "k" -> 5, "l" -> 1, "m" -> 3, "n" -> 1, "o" -> 1,
      "p" -> 3, "q" -> 10, "r" -> 1, "s" -> 1, "t" -> 1, "u" -> 1, "v" -> 4,
      "w" -> 4, "x" -> 8, "y" -> 4, "z" -> 10))
  }

}
