package csc424.lambda

import java.io.Reader

import csc424.simplide._

/**
 * This object hooks up the pieces of our interpreter.
 *
 * @author bhoward
 */
object Language extends SimpleLanguage {
  type AST = Expr
  type State = Expr
  type Result = Expr

  def parse(in: Reader) = Parser.parse(in)

  def init(ast: AST) = ast

  def run(state: State, context: ExecutionContext) = {
    val interpreter = new Interpreter(context)
    var e = state
    while (!interpreter.normalForm(e)) {
      e = interpreter.eval(e)
    }
    e
  }

  def showResult(result: Result): String = result.toString
}