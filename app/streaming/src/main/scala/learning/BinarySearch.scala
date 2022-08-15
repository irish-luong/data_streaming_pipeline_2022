package learning

import scala.annotation.tailrec

object BinarySearch {

  @tailrec
  def find(arr: List[Int], value: Int, max: Int= -1, min: Int= -1): Option[Int] = {

    val upper: Int = if (max <= -1) arr.length - 1 else max;
    val lower: Int = if (max <= -1) 0 else min;

    // Catch condition value out of range
    if (arr.isEmpty) {
      return None;
    }
    if (value < arr(lower)) {
      return None
    } else if (value > arr(upper)){
      return None
    }
    // Stop condition for recursive function
    if (upper < lower) {
      return None
    } else if (upper == lower) {
      if (arr(upper) == value) return Option(upper) else return None;
    }
    // Find out the middle value of array
    val mid: Int = (lower + upper) / 2;

    if (arr(mid) == value) {
      Option(mid);
    } else if (arr(mid) > value) {
      find(arr, value, mid - 1, lower)
    } else {
      find(arr, value, upper, lower + 1)
    }
  }
}
