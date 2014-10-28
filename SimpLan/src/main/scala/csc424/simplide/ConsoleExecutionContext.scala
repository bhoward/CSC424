package csc424.simplide

import java.util.Scanner
import java.io.PrintWriter

class ConsoleExecutionContext(val input: Scanner = new Scanner(Console.in),
    val output: PrintWriter = new PrintWriter(Console.out),
    val debugFlag: Boolean = false) extends ExecutionContext {
  var step = 0
  
  def performStep(description: String) {
    step += 1
    if (debugFlag) {
      println("Step: " + step)
      println(description)
    }
  }
  
  def printError(message: String) {
    Console.err.println(message)
  }
}