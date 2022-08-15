package learning

object FlattenArray {

  def flatten(arr: List[Any]): List[Any] = {
    arr.filter(x => x != null) flatMap {
              case v: List[Any] => flatten(v);
              case v => List(v);
            }
  }

  def main(args: Array[String]): Unit = {
    println(flatten(List(1, List(2, 3, 4, 5, 6, 7), 8)))
  }
}
