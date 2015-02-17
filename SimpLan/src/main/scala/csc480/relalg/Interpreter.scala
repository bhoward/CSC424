package csc480.relalg

import csc424.simplide._
import Language._

/**
 * The Interpreter defines the functions needed to interpret
 * an abstract syntax tree (Expr, BExpr, Stmt, Decl) in the Control Language.
 * 
 * @param context the ExecutionContext to use for input/output
 */
class Interpreter(context: ExecutionContext) {
  /**
   * Evaluate an Expr in the given Environment.
   *
   * @param expr the abstract syntax tree for the expression
   * @param env the Environment binding ids to cells
   * @return the value of the expression
   */
  def eval(cmds: AST, env: EnvType): Result = {
    val env2 = new ChildEnvironment(env)
    cmds foreach {_.eval(env2, context)}
  }
}
