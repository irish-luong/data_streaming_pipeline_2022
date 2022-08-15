package models

trait User {
  def username: String
}

trait Tweeter {
  this: User =>  // reassign this
  def tweet(tweetText: String): Unit = println(s"$username: $tweetText")
}

class VerifiedTweeter(val username_ : String) extends Tweeter with User {
  def username = s"real $username_";
}

object ClassRun {
  def main(args: Array[String]): Unit = {
    val realBeyonce = new VerifiedTweeter("Beyoncé")
    realBeyonce.tweet("Just spilled my glass of lemonade")  // prints "
  }
}

