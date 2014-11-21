package csc424.lambda

// Abstract Syntax Tree for Lambda Expressions
sealed trait Expr

case class FunExpr(param: String, body: Expr) extends Expr {
  override def toString = "(" + param + " => " + body + ")"
}

case class AppExpr(fun: Expr, arg: Expr) extends Expr {
  override def toString = "(" + fun + " " + arg + ")"
}

case class IdExpr(id: String) extends Expr {
  override def toString = id
}
