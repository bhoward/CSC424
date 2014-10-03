package csc424.ctrllang

import csc424.simplide.SimpleLanguage
import java.io.Reader
import csc424.simplide.ExecutionContext

object Language extends SimpleLanguage {
  type AST = Expr
  type State = (AST, Interpreter)
  type Result = ValueType
  
  def parse(in: Reader): AST = Parser.parse(in)
  
  def init(ast: AST, context: ExecutionContext): State =
    (ast, new Interpreter(context))

  def run(state: State): Result = {
    val (ast, interpreter) = state
    interpreter.eval(ast, EmptyEnvironment)
  }
}