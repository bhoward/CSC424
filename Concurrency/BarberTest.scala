//object BarberTest {
//  def main(args : Array[String]) {
//    val random = new scala.util.Random
//
//    Barber.start
//    WaitingRoom.start
//    val customers = List(
//        new Customer("Alice"),
//        new Customer("Bob"),
//        new Customer("Carol"),
//        new Customer("Dave"),
//        new Customer("Edith"),
//        new Customer("Fred"),
//        new Customer("Gina"),
//        new Customer("Harold"),
//        new Customer("Irma"),
//        new Customer("Joe")
//    )
//
//    for (c <- customers) {
//      c.start
//      WaitingRoom ! Enter(c)
//      // Customers arrive every 0 to 4 seconds (several might arrive at once!)
//      Thread.sleep(random.nextInt(5) * 1000)
//    }
//  }
//}