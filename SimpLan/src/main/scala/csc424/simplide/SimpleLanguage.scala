package csc424.simplide

import java.io._
import java.util.Scanner

/**
 * Extend this trait to describe a simple interpreted language.
 *
 * @author bhoward
 */
trait SimpleLanguage {
  /**
   * The type of abstract syntax trees (the result of parsing)
   */
  type AST

  /**
   * The type of interpreter states (environment, stack, ...)
   */
  type State

  /**
   * The type produced at the end of running a program
   */
  type Result

  /**
   * Parse a program from the given input reader.
   * May throw InterpreterException(message) on error.
   *
   * @param in
   * @return an abstract syntax tree
   */
  def parse(in: Reader): AST

  /**
   * Construct an initial state from an abstract syntax tree.
   * This is the appropriate time to do static analysis, such as type-checks.
   * May throw InterpreterException(message) on error.
   *
   * @param ast
   * @return the initial state
   */
  def init(ast: AST): State

  /**
   * Execute the program from the given state.
   * Before each "step", call performStep(description) on the ExecutionContext.
   * May throw InterpreterException(message) on error.
   *
   * @param state
   * @param context
   * @return the result of execution
   */
  def run(state: State, context: ExecutionContext): Result
  
  /**
   * Convert a result to a string.
   * 
   * @param result
   * @return a String representation of the result
   */
  def showResult(result: Result): String

  /**
   * Convenience method to parse a program from the given string.
   * May throw InterpreterException(message) on error.
   *
   * @param src
   * @return an abstract syntax tree
   */
  def parse(src: String): AST = parse(new StringReader(src))

  /**
   * Convenience method to run the interpreter on a program, with console I/O.
   *
   * @param src the program to be interpreted
   * @return the result of interpreting the program
   */
  def apply(src: String): Result = {
    val out = new PrintWriter(scala.Console.out)
    val context = new ConsoleExecutionContext(new Scanner(scala.Console.in), out)
    val ast = parse(src)
    val state = init(ast)
    val result = run(state, context)
    out.flush()
    result
  }
}