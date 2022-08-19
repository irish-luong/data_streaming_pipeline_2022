package learning

import scala.collection.immutable

object SumRecursive {

  def sumA(numberList: List[Int]): Int = numberList match {
    case List(a) => a
    case List(a, _*) => a + sumA(numberList.tail)
    }

  def sumB(numberList: List[Int]): Int = numberList match {
    case Nil => 0
    case x :: tail => x + sumB(tail)
  }

  def main(args: Array[String]): Unit = {
      val myList: List[Int] = immutable.List(1,2,3,4,5);
      println(sumB(myList));
  }
}
