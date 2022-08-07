package models

import Array._
class Class2School {

  var orders: Array[Student] = Array();

  def register(student: Student): Unit = {
    orders = orders :+ student
  }
  def showStudent(): Unit = {
    for (student <- orders) {
      student.printName();
    }
  }
}

case class Student(name: String, age: Int) {
  def printName(): Unit = {
    println(name);
  }
}

object MainApp {
  def main(args: Array[String]): Unit = {
    val runClass = new Class2School();
//   Declare student
    val student_a = Student("A", 26);
    val student_b = Student("B", 25);

    for (i <- 1 to 3) {
      i match {
        case 1 => runClass.register(student_a)
        case 2 => runClass.register(student_b)
        case _ => println("Not handle")
      }
    }

    runClass.showStudent();
  }
}