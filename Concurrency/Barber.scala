object Barber extends Actor {
  val random = new scala.util.Random

  def cutHair(customer: Customer) {
    customer ! Start
    println("Barber: Processing customer " + customer)
    // Randomly take from 1 to 10 seconds
    Thread.sleep((random.nextInt(10) + 1) * 1000)
    customer ! Done
  }

  def act = loop {
    react {
      case Enter(customer) => {
        cutHair(customer)
        WaitingRoom ! Next
      }

      case Wait => {
        println("Barber: No customers. Going to have a sleep")
      }
    }
  }
}