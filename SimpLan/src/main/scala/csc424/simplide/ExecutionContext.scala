package csc424.simplide

import java.io.PrintWriter
import java.util.Scanner

trait ExecutionContext {
  val input: Scanner
  val output: PrintWriter
  def debugFlag: Boolean

  def performStep(description: String): Unit

  def printError(message: String): Unit
}