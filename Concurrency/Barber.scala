import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.ActorDSL
import akka.actor.Props

class Barber extends Actor {
  val random = new scala.util.Random

  def cutHair(customer: ActorRef) {
    customer ! Start
    println("Barber: Processing customer " + customer)
    // Randomly take from 1 to 10 seconds
    Thread.sleep((random.nextInt(10) + 1) * 1000)
    customer ! Done
  }

  def receive = {
    case Enter(customer) => {
      cutHair(customer)
      WaitingRoom.ref ! Next
    }

    case Wait => {
      println("Barber: No customers. Going to have a sleep")
    }

  }
}

object Barber {
  val system = ActorSystem("BarberShop")

  val ref = system.actorOf(Props(new Barber), name = "theBarber")
}