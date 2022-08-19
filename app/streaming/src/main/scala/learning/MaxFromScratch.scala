package learning

import scala.annotation.tailrec

object MaxFromScratch {

  def mainMax(a: Int, b: Int, c: Int): Int  = {

    @tailrec
    def max(values: List[Int]): List[Int] = values match {
      case List(a, b) => {
        List(if (a > b) a else b);
      }
      case List(a, b, _*) => {
        val tmpMax = if(a > b) a else b;
        max(values.tail.updated(0, tmpMax));
      }
      case List(a) => List(a);
      case _ => throw new Exception("Not handle this case")
    }

    val values: List[Int] = List(10, a, b, c, 4)
    max(values).head

  }
  def main(args: Array[String]): Unit = {

    println(mainMax(1, 2, 3))
  }
}
