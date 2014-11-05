object TimedTest {
  /**
   * @param block the block to be timed
   * @return      the result paired with how long it took (in milliseconds)
   */
  def time[T](block: => T): (T, Long) = {
    val start = System.currentTimeMillis
    val result = block
    val finish = System.currentTimeMillis
    (result, finish - start)
  }

  def main(args: Array[String]) {
    for (_ <- 1 to 2) {
      // Run it at least twice to allow the JVM to "warm up"
      val (result, elapsed) = time {
        // TODO Your Block Here
      }
     
      println("Computed " + result + " in " + elapsed + " ms")
    }
  }
}