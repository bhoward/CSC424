package csc424.simplide

import java.io.{ PrintWriter, FileReader }
import java.util.Scanner

class ConsoleDriver(interpreter: SimpleLanguage) {
  private def findSourceFileName(args: Array[String]): Option[String] =
    args find { arg => !(arg startsWith "--") }

  private def findDebugFlag(args: Array[String]): Boolean = args contains "--debug"

  def start(args: Array[String]) {
    val sourceFileName = findSourceFileName(args) getOrElse {
      System.err.println("Missing source file name")
      System.exit(1)
      ""
    }

    val debugFlag = findDebugFlag(args)

    try {
      val out = new PrintWriter(Console.out)
      val context = new ConsoleExecutionContext(new Scanner(Console.in), out, debugFlag)
      val ast = interpreter.parse(new FileReader(sourceFileName))
      val state = interpreter.init(ast, context)
      val result = interpreter.run(state)
      out.println("DONE: " + result)
      out.flush()
    } catch {
      case e: Exception => Console.err.println(e.getMessage)
    }
  }
}