package csc424.ctrllang

import csc424.simplide._
import java.io.Reader
import java.util.Scanner

object Language extends SimpleLanguage {
  type AST = Expr
  type ValueType = Cell
  type EnvType = Environment[Cell]
  type State = (AST, EnvType)
  type Result = Double
  
  def parse(in: Reader): AST = Parser.parse(in)
  
  def init(ast: AST): State = (ast, EmptyEnvironment)

  def run(state: State, context: ExecutionContext): Result = {
    val interpreter = new Interpreter(context)
    val (ast, env) = state
    interpreter.eval(ast, env)
  }
  
  private val decimalFormat = new java.text.DecimalFormat("0.##########")
  def showResult(result: Result): String = decimalFormat.format(result)
  
  def read(in: Scanner): Result = in.nextDouble
}