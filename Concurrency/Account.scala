import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._

case class Deposit(amount: Int)
case object Balance

class Account extends Actor {
  var balance = 0

  def receive = {
    case Deposit(amount) => balance += amount
    case Balance => sender ! balance
  }
}

object AccountDemo extends App {
  val system = ActorSystem("AccountSystem")
  implicit val timeout = Timeout(5 seconds)

  val accountActor = system.actorOf(Props(new Account), name = "myAccount")
  
  accountActor ! Deposit(100)
  val balance = Await.result(accountActor ? Balance, timeout.duration)
  println(balance)
}