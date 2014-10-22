package csc424.ctrllang

import csc424.simplide.SimpleLanguage
import java.io.Reader
import csc424.simplide.ExecutionContext

object Language extends SimpleLanguage {
  type AST = Expr
  type State = (AST, Environment)
  type Result = ValueType
  
  def parse(in: Reader): AST = Parser.parse(in)
  
  def init(ast: AST): State = (ast, EmptyEnvironment)

  def run(state: State, context: ExecutionContext): Result = {
    val interpreter = new Interpreter(context)
    val (ast, env) = state
    interpreter.eval(ast, env)
  }
  
  def showResult(result: Result): String = show(result)
}