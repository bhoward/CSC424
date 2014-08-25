object HelloAll2S {
  def main(args: Array[String]): Unit = {
    for (i <- 0 until args.length) {
      println("Hello, " + args(i))
    }
  }
}
