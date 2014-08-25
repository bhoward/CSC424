object HelloAllS {
  def main(args: Array[String]): Unit = {
    var i = 0
    while (i < args.length) {
      println("Hello, " + args(i))
      i += 1
    }
  }
}
