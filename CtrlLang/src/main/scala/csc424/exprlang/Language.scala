package csc424.exprlang

import csc424.simplide._

import java.io.Reader

object Language extends SimpleLanguage {
  type AST = Expr
  type State = (AST, EnvType)
  type Result = Int
  
  type EnvType = Environment[ValueType]
  
  type ValueType = Result
  
  def parse(in: Reader): AST = Parser.parse(in)
  
  def init(ast: AST): State = (ast, EmptyEnvironment)

  def run(state: State, context: ExecutionContext): Result = {
    val interpreter = new Interpreter(context)
    val (ast, env) = state
    interpreter.eval(ast, env)
  }
  
  def showResult(result: Result): String = result.toString
}