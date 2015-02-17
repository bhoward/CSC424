package csc424.relalg

import csc424.simplide._
import java.io.Reader
import java.util.Scanner

object Language extends SimpleLanguage {
  type AST = List[Command]
  type State = (AST, EnvType)
  type Result = Unit
  
  type EnvType = Environment[Table]
  
  def parse(in: Reader): AST = Parser.parse(in)
  
  def init(ast: AST): State = (ast, EmptyEnvironment)

  def run(state: State, context: ExecutionContext): Result = {
    val interpreter = new Interpreter(context)
    val (ast, env) = state
    interpreter.eval(ast, env)
  }
  
  def showResult(result: Result): String = ""
}