object BarberTest {
  def main(args : Array[String]) {
    val random = new scala.util.Random

    val customers = List(
        Customer("Alice"),
        Customer("Bob"),
        Customer("Carol"),
        Customer("Dave"),
        Customer("Edith"),
        Customer("Fred"),
        Customer("Gina"),
        Customer("Harold"),
        Customer("Irma"),
        Customer("Joe")
    )

    for (c <- customers) {
      WaitingRoom.ref ! Enter(c)
      // Customers arrive every 0 to 4 seconds (several might arrive at once!)
      Thread.sleep(random.nextInt(5) * 1000)
    }
  }
}