package csc424.lambda

import csc424.simplide._

class Interpreter(context: ExecutionContext) {
  def normalForm(expr: Expr): Boolean = expr match {
    case AppExpr(FunExpr(_, _), _) => false
    case AppExpr(fun, arg) => normalForm(fun) && normalForm(arg)
    case _ => true
  }

  def eval(expr: Expr): Expr = {
    context.performStep("Eval: " + expr)
    expr match {
      case AppExpr(FunExpr(param, body), arg) => subst(body, param, arg)
      case AppExpr(fun, arg) => if (normalForm(fun)) AppExpr(fun, eval(arg))
      else AppExpr(eval(fun), arg)
      case _ => expr
    }
  }

  def subst(expr: Expr, param: String, arg: Expr): Expr = expr match {
    case IdExpr(id) =>
      if (id == param)
        arg
      else
        expr
    case AppExpr(e1, e2) =>
      AppExpr(subst(e1, param, arg), subst(e2, param, arg))
    case FunExpr(id, body) =>
      if (id == param)
        expr
      else {
        // Avoid variable capture by finding a new local name
        var newId = id
        var n = 0
        while (contains(arg, newId)) {
          n += 1
          newId = id + n
        }
        val newBody = if (id == newId) body else subst(body, id, IdExpr(newId))
        FunExpr(newId, subst(newBody, param, arg))
      }
  }

  def contains(expr: Expr, name: String): Boolean = expr match {
    case IdExpr(id) => name == id
    case AppExpr(e1, e2) => contains(e1, name) || contains(e2, name)
    case FunExpr(id, body) => id != name && contains(body, name)
  }
}
