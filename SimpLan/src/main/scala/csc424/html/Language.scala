package csc424.html

import java.io.Reader

import csc424.simplide._

/**
 * This object hooks up the pieces of our interpreter.
 *
 * @author bhoward
 */
object Language extends SimpleLanguage {
  type AST = Table
  type State = Table
  type Result = List[String]

  def parse(in: Reader) = Parser.parse(in)

  def init(ast: AST) = ast

  def run(state: State, context: ExecutionContext) = {
    state.render()
  }

  def showResult(result: Result): String = "\n" + result.mkString("\n")
}