import akka.actor.Actor
import akka.actor.Props

class Customer(name: String) extends Actor {
  def receive = {
    case Full => {
      println("Customer: " + name + ": The waiting room is full; I am leaving.")
      context.stop(self)
    }

    case Wait => println("Customer: " + name + ": I will wait.")

    case Start => println("Customer: " + name + ": I am now being served.")

    case Done => {
      println("Customer: " + name + ": I have been served.")
      context.stop(self)
    }
  }

  override def toString = name
}

object Customer {
  def apply(name: String) = Barber.system.actorOf(Props(new Customer(name)), name = name)
}