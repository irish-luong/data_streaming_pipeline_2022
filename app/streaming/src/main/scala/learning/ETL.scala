package learning

import scala.collection.immutable

object ETL {

  def transform(input: immutable.Map[Int, Seq[String]]): immutable.Map[String, Int] = {
    (input map {
      data => {
        data._2
          .map(element => immutable.Map(element.toLowerCase() -> data._1))
          .reduce((x, y) => x.++(y));
      }
    }).reduce(
      (x, y) => x.++(y)
    )
  }

  def main(args: Array[String]): Unit = {

    val input = immutable.Map[Int, Seq[String]](
      1 -> Seq("A", "E"),
      2 -> Seq("D", "G")
    )
    println(transform(input));
  }
}
