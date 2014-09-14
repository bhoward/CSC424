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
   * Parse a program from the given string (convenience method).
   * May throw InterpreterException(message) on error.
   *
   * @param src
   * @return an abstract syntax tree
   */
  def parse(src: String): AST = parse(new StringReader(src))

  /**
   * Construct an initial state from an abstract syntax tree and ExecutionContext.
   * This is the appropriate time to do static analysis, such as type-checks.
   * May throw InterpreterException(message) on error.
   *
   * @param ast
   * @param context
   * @return the initial state
   */
  def init(ast: AST, context: ExecutionContext): State

  /**
   * Execute the program from the given state.
   * Before each "step", call performStep(description) on the ExecutionContext.
   * May throw InterpreterException(message) on error.
   *
   * @param state
   * @return the result of execution
   */
  def run(state: State): Result

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
    val state = init(ast, context)
    val result = run(state)
    out.flush()
    result
  }
}