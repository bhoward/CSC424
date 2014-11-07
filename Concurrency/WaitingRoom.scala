import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props

class WaitingRoom extends Actor {
  import scala.collection.mutable.Queue

  val waitingCustomers = new Queue[ActorRef]
  val capacity = 2 // waiting room has this many chairs
  var barberAsleep = true

  def receive = {
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
        Barber.ref ! Enter(waitingCustomers.dequeue)
      } else {
        Barber.ref ! Wait
        barberAsleep = true
      }
    }
  }
}

object WaitingRoom {
  val ref = Barber.system.actorOf(Props(new WaitingRoom), name = "theWaitingRoom")
}