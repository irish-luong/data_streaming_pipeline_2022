package learning

object Twofer {
  def twofer(name: String = null): String = {
    if (name == "" || name == null) {
      "One for you, one for me."
    } else {
      s"One for $name, one for me."
    }
  }

  def main(args: Array[String]): Unit = {
    println(twofer("hello"));
  }
}

