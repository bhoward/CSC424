package csc424.lambda

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import scala.language.reflectiveCalls
import csc424.simplide.ConsoleExecutionContext
import java.util.Scanner
import java.io.PrintWriter

@RunWith(classOf[JUnitRunner])
class InterpreterTest extends Specification {
  def withInput[T](source: String)(block: => T): T = {
    val mockIn = new java.io.StringReader(source)
    val result = Console.withIn(mockIn) {
      block
    }
    mockIn.read must_== -1 // all input was consumed
    result
  }
  
  def withOutput[T](target: String*)(block: => T): T = {
    val mockOut = new java.io.PrintStream(new java.io.ByteArrayOutputStream) {
      def must_==(lines: List[String]) =
        out.toString must_== lines.mkString(System.getProperty("line.separator"))
    }
    val result = Console.withOut(mockOut) {
      block
    }
    mockOut must_== target.toList
    result
  }
  
  def eval(src: String) = {
    Language(src)
  }
  
  def time(expr: => Unit): Long = {
    val start = System.currentTimeMillis
    expr
    val end = System.currentTimeMillis
    end - start
  }

  "Simple expressions can be evaluated" in {
    eval("(x => x) A") must_== IdExpr("A")
  }

  "A table of running times is displayed" in {
    def generate(n: Int): Expr =
      if (n == 0)
        IdExpr("X0")
      else
        AppExpr(FunExpr("x" + n, generate(n - 1)), IdExpr("X" + n))
    
    val ctx = new ConsoleExecutionContext(new Scanner(scala.Console.in), new PrintWriter(scala.Console.out))
    for (i <- 10 to 100 by 10) {
      val e = generate(i)
      val t = time {
        Language.run(e, ctx) must_== IdExpr("X0")
      }
      println(i + ": " + t)
    }
    
    {} must_== {}
  }
}