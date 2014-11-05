import scala.actors.Actor
import scala.actors.Actor._

case class Enter(customer: Customer)
case object Wait
case object Next
case object Full
case object Start
case object Done
