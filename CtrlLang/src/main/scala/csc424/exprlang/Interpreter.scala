package csc424.exprlang

import csc424.simplide._
import Language._

/**
 * The Interpreter defines the functions needed to interpret
 * an abstract syntax tree (Expr, Stmt, Decl) in the Expression Language.
 * 
 * @param context the ExecutionContext to use for input/output
 */
class Interpreter(context: ExecutionContext) {
  /**
   * Evaluate an Expr in the given Environment.
   *
   * @param expr the abstract syntax tree for the expression
   * @param env the Environment binding ids to values
   * @return the value of the expression
   */
  def eval(expr: Expr, env: EnvType): ValueType = expr match {
    case NumExpr(n) => n
    case IdExpr(id) => env(id)
    case UnOpExpr("-", e) => -eval(e, env)
    case BinOpExpr("+", e1, e2) => eval(e1, env) + eval(e2, env)
    case BinOpExpr("-", e1, e2) => eval(e1, env) - eval(e2, env)
    case BinOpExpr("*", e1, e2) => eval(e1, env) * eval(e2, env)
    case BinOpExpr("/", e1, e2) => eval(e1, env) / eval(e2, env)
    case BinOpExpr("%", e1, e2) => eval(e1, env) % eval(e2, env)
    case LetExpr(ds, e) => eval(e, elaborate(ds, env))
    case _ => sys.error("Unrecognized expression: " + expr)
  }

  /**
   * Process a list of declarations in the given Environment, producing
   * a new (child) Environment containing the appropriate bindings.
   *
   * @param ds the abstract syntax trees of the declarations
   * @param env the (parent) Environment binding ids to values
   * @return the new elaborated Environment
   */
  def elaborate(ds: List[Decl], env: EnvType): EnvType = {
    val child = new ChildEnvironment(env)
    
    for (decl <- ds) {
      decl match {
        // Evaluate each initial value in the parent Environment, and
        // bind the id to that value in the child
        case ValDecl(id, e) => child.addBinding(id, eval(e, env))
      }
    }
    
    child
  }
}