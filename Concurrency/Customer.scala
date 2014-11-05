class Customer(name: String) extends Actor {
  def act = loop { 
    react {
      case Full => {
        println("Customer: " + name + ": The waiting room is full; I am leaving.")
        exit
      }
 
      case Wait => println("Customer: " + name + ": I will wait.")
 
      case Start => println("Customer: " + name + ": I am now being served.")
      
      case Done => {
        println("Customer: " + name + ": I have been served.")
        exit
      }
    }
  }
    
  override def toString = name
}