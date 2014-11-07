import akka.actor.ActorRef

case class Enter(customer: ActorRef)
case object Wait
case object Next
case object Full
case object Start
case object Done
