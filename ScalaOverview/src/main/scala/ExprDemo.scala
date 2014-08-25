import java.io.PrintStream

object ExprDemo {
  trait Expr {
    def eval(): Int
    def accept(visitor: Visitor): Unit
  }
  
  class Const_Expr(val value: Int) extends Expr {
    def eval(): Int = value
    def accept(visitor: Visitor): Unit =  visitor.visit(this)
  }
  
  class Plus_Expr(val left: Expr, val right: Expr) extends Expr {
    def eval(): Int = left.eval() + right.eval()
    def accept(visitor: Visitor): Unit = visitor.visit(this)
  }
  
  // Define other operators similarly: Minus_Expr, Times_Expr, ...
  
  trait Visitor {
    def visit(const_expr: Const_Expr): Unit
    def visit(plus_expr: Plus_Expr): Unit
  }
  
  class Infix_Visitor(out: PrintStream) extends Visitor {
    def visit(const_expr: Const_Expr): Unit = {
      out.print(const_expr.value)
    }
  
    def visit(plus_expr: Plus_Expr): Unit = {
      out.print("(")
      plus_expr.left.accept(this)
      out.print("+")
      plus_expr.right.accept(this)
      out.print(")")
    }
  }
  
  class Postfix_Visitor(out: PrintStream) extends Visitor {
    def visit(const_expr: Const_Expr): Unit = {
      out.print(const_expr.value + " ")
    }
  
    def visit(plus_expr: Plus_Expr): Unit = {
      plus_expr.left.accept(this)
      plus_expr.right.accept(this)
      out.print("+ ")
    }
  }
  
  def main(args: Array[String]) = {
    val e1 = new Plus_Expr(new Const_Expr(7), new Const_Expr(14))
    val e2 = new Const_Expr(21)
    val expr = new Plus_Expr(e1, e2)

    println("Value is: " + expr.eval())

    val infix = new Infix_Visitor(Console.out)
    print("Infix is: ")
    expr.accept(infix)
    println()

    val postfix = new Postfix_Visitor(Console.out)
    print("Postfix is: ")
    expr.accept(postfix)
    println()
  }
}
