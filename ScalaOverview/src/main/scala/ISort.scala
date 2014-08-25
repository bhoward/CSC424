object ISort {
  // Functions may be defined in any order, but must belong to an object
  def main(args: Array[String]): Unit = {
    val data = Array(3, 1, 4, 1, 5, 9, 2, 6, 5)
    isort(data)
    display(data)
  }
  
  def isort(a: Array[Int]): Unit = {
    for (i <- 1 until a.length) {
      val value = a(i)
      var j = i
      while (j > 0 && a(j-1) > value) {
        a(j) = a(j-1)
        j -= 1
      }
      a(j) = value
    }
  }
  
  def display(a: Array[Int]): Unit = {
    for (i <- 0 until a.length) {
      print(a(i) + " ")
    }
    println()
  }
}
