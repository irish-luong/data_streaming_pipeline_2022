package learning

import scala.jdk.Accumulator

object SquareRootNewton {

  /*
  * Return absolute value of
  * a given number
  * Ex: |-5| = |5| = 5
  * */
  def abs(x: Double): Double = if (x < 0) -x else x;
  /*
  * will let you know is the average is close enough
  * to the actual value by compare delta with a threshold
  * Params:
  *   guess: value of this guess
  *   previousGuess: value of the last guess
  *   threshold: base value if delta less than or equal to this value
  *             seem to be no difference between current guess and previous guess
  * Return:
  *   boolean value for whether has any difference between two times guess
  * */

  /*
  * Return a new average
  * value by given previous guess and original value
  * */

  /*
  * 1. Initialize a guess, usually 1
  * 2. Get a first improvement result by the initial guess
  * 3. If the delta good enough -> return guess
  * 4. Else continue call improve function
  * */


  def sqrt(x: Double, threshold: Double = 0.0001): Double = {

    def improve(avg: Double): Double = (avg + (x / avg))/ 2

    def isGoodEnough(guess: Double, previousGuess: Double, threshold: Double): Boolean = {
      val delta = abs(guess - previousGuess) / previousGuess;
      if (delta <= threshold) true else false;
    }

    def sqrtIterative(guess: Double, threshold: Double): Double = {

      // Take a new value from improve function
      val improveValue = improve(guess);

      if (isGoodEnough(guess, improveValue, threshold)) {
        guess;
      } else {
        sqrtIterative(improveValue, threshold);
      }

    }
      sqrtIterative(1, threshold)
  }


  def factorial(x: Int): Int = {

    def loop(accumulator: Int, x: Int): Int = {

      if (x == 0){
        accumulator
      } else {
        loop(accumulator * x, x - 1)
      }
    }

    loop(1, x)
  }

  def main(args: Array[String]) : Unit = {
//    println(sqrt(4));
    println(factorial(5));
  }
}
