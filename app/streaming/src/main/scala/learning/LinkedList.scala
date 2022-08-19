//package learning
//import learning.SimpleLinkedList;
//
//

sealed trait SimpleLinkedList[T] {

  def isEmpty: Boolean
  def value: T
  def add(item: T): SimpleLinkedList[T]
  def next: SimpleLinkedList[T]
  def reverse: SimpleLinkedList[T]
  def toSeq: Seq[T]
}

//sealed class Node[T](val value: T, var next: Node[T]);
//
//class LinkedList extends SimpleLinkedList[Int] {
//
//  /*
//  * Implement method check head of linked list
//  * If head is null then this linked list has no node
//  * */
//  override def isEmpty: Boolean = {
//    value match {
//      case Some(_) => false
//      case _ => true
//    }
//  }
//
//  /*
//  * Function get the latest value added to Linked list
//  * If list is empty function will throw an exception
//  * */
//    def value: Int = value
//
//  /*
//  * Add a new node to list
//  * 1. Create a new node
//  * 2. Set next of this node is head
//  * 3. Set value of head is this node
//  * */
//  override def add(item: Int): SimpleLinkedList[Int] = {
//
//    var _next: SimpleLinkedList[Int] = next
//
//    case _next match {
//      case Some(_) => {
//
//      }
//      case _ => _next.next = LinkedList()
//    }
//  }
//
//  override def next: SimpleLinkedList[Int] = {
//
//
//  }
//
//  override def reverse: SimpleLinkedList[Int] = {
//
//  }
//
//  def toSeq: Seq[T]
//}
