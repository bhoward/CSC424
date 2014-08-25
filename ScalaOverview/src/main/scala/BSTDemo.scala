object BST {
  sealed trait Tree
  case object Empty extends Tree
  case class Node(smaller: Tree, value: Int, larger: Tree) extends Tree

  def insert(t: Tree, x: Int): Tree = t match {
    case Empty => Node(Empty, x, Empty)
    case Node(smaller, value, larger) =>
      if (x < value)
        Node(insert(smaller, x), value, larger)
      else if (x > value)
        Node(smaller, value, insert(larger, x))
      else t // ignore duplicates
  }

  def insertAll(t: Tree, xs: List[Int]): Tree = xs match {
    case Nil => t
    case head::tail => insertAll(insert(t, head), tail)
  }

  def contains(t: Tree, x: Int): Boolean = t match {
    case Empty => false
    case Node(smaller, value, larger) =>
      if (x < value)
        contains(smaller, x)
      else if (x > value)
        contains(larger, x)
      else true
  }

  def traverse[T](t: Tree, e: T, n: (T, Int, T) => T): T = t match {
    case Empty => e
    case Node(smaller, value, larger) =>
      n(traverse(smaller, e, n), value, traverse(larger, e, n))
  }
}

object BSTDemo {
  import BST._
  
  def main(args: Array[String]): Unit = {
    val tree = insertAll(Empty, List(3, 1, 4, 1, 5, 9, 2, 6, 5))
    for (i <- 1 to 9)
      println(s"Contains $i: ${contains(tree, i)}")
      
    val preorder =
      traverse[List[Int]](tree, Nil, (s, v, l) => (v :: s) ::: l)
    val inorder =
      traverse[List[Int]](tree, Nil, (s, v, l) => (s :+ v) ::: l)
    println("Preorder: " + preorder)
    println("Inorder: " + inorder)
  }
}
