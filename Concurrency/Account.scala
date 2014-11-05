import scala.actors.Actor

case class Deposit(amount: Int)
case object Balance

class Account extends Actor {
  var balance = 0

  def act = Actor.loop {
    react {
      case Deposit(amount) => balance += amount
      case Balance => reply(balance)
    }
  }
}