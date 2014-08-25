import java.io.PrintStream

object ExprDemo2 {
  trait Expr {
    def eval(): Int
  }
  
  case class Const_Expr(value: Int) extends Expr {
    def eval(): Int = value
  }
  
  case class Plus_Expr(left: Expr, right: Expr) extends Expr {
    def eval(): Int = left.eval() + right.eval()
  }
  
  // Define other operators similarly: Minus_Expr, Times_Expr, ...
  
  def infix(expr: Expr, out: PrintStream): Unit = expr match {
    case Const_Expr(value) =>
      out.print(value)

    case Plus_Expr(left, right) =>
      out.print("(")
      infix(left, out)
      out.print("+")
      infix(right, out)
      out.print(")")
  }

  def postfix(expr: Expr, out: PrintStream): Unit = expr match {
    case Const_Expr(value) =>
      out.print(value + " ")

    case Plus_Expr(left, right) =>
      postfix(left, out)
      postfix(right, out)
      out.print("+ ")
  }

  def main(args: Array[String]) = {
    val e1 = new Plus_Expr(new Const_Expr(7), new Const_Expr(14))
    val e2 = new Const_Expr(21)
    val expr = new Plus_Expr(e1, e2)

    println("Value is: " + expr.eval())

    print("Infix is: ")
    infix(expr, Console.out)
    println()

    print("Postfix is: ")
    postfix(expr, Console.out)
    println()
  }
}
