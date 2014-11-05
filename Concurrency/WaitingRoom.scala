object WaitingRoom extends Actor {
  import scala.collection.mutable.Queue

  val waitingCustomers = new Queue[Customer]
  val capacity = 2 // waiting room has this many chairs
  var barberAsleep = true

  def act = loop {
    react {
      case Enter(customer) => {
        if (waitingCustomers.size == capacity) {
          customer ! Full
        } else {
          waitingCustomers += customer
          if (barberAsleep) {
            // the only waiting customer should be the new arrival
            assert(waitingCustomers.size == 1)
            barberAsleep = false
            self ! Next
          } else {
            customer ! Wait
          }
        }
      }

      case Next => {
        if (!waitingCustomers.isEmpty) {
          Barber ! Enter(waitingCustomers.dequeue)
        } else {
          Barber ! Wait
          barberAsleep = true
        }
      }
    }
  }
}