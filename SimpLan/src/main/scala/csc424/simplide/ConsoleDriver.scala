package csc424.simplide

import java.io.{ PrintWriter, FileReader }
import java.util.Scanner

class ConsoleDriver(languages: (String, SimpleLanguage)*) {
  private def findSourceFileName(args: Array[String]): Option[String] =
    args find { arg => !(arg startsWith "--") }

  private def findDebugFlag(args: Array[String]): Boolean = args contains "--debug"
  
  private def chooseLanguage(args: Array[String]): SimpleLanguage = {
    val choice = for ((flag, language) <- languages if args contains flag) yield language
    (choice :+ languages.head._2).head
  }

  def start(args: Array[String]) {
    val sourceFileName = findSourceFileName(args) getOrElse {
      System.err.println("Missing source file name")
      System.exit(1)
      ""
    }

    val debugFlag = findDebugFlag(args)
    
    val language = chooseLanguage(args)

    try {
      val in = new Scanner(Console.in)
      val out = new PrintWriter(Console.out)
      val context = new ConsoleExecutionContext(in, out, debugFlag)
      val ast = language.parse(new FileReader(sourceFileName))
      val state = language.init(ast)
      val result = language.run(state, context)
      out.println("DONE: " + language.showResult(result))
      out.flush()
    } catch {
      case e: Exception => Console.err.println(e.getMessage)
    }
  }
}