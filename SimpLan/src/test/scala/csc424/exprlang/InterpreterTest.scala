package csc424.exprlang

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

import scala.language.reflectiveCalls 

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
//    val ast = Parser(src).get
//    Interpreter.eval(ast, EmptyEnvironment)
    Language(src)
  }

  "Simple expressions can be evaluated" in {
    eval("1+2*3") must_== 7
  }

  "Let expressions allow binding of vals" in {
    val src = "let val a = 1 val b = 2 val c = 3 in a + b * c"
    eval(src) must_== 7
  }

  "Multiline expressions work just as well" in {
    val src = """let
                |  val a = 1
                |  val b = 2
                |  val c = 3
                |in a + b * c""".stripMargin
    eval(src) must_== 7
  }

//  "Simple expressions with floats can be evaluated" in {
//    eval("1+2.0*0.3e+1") must_== 7
//  }
//
//  "Floating point values work" in {
//  	val src = "1.2 * 3 - 26e-1"
//  	eval(src) must be closeTo(1.0 +/- 0.0001)
//  }
//
//  "Unary functions are evaluated" in {
//  	val src = "sqrt(2) + log(1)"
//  	eval(src) must be closeTo(1.4142 +/- 0.0001)
//  }
//  
//  "Binary functions are evaluated" in {
//  	val src = "max(79, 37) - min(79, 37)"
//  	eval(src) must_== 42
//  }
  
  "Undeclared identifiers are caught" in {
  	val src = "a + b * c"
  	eval(src) must throwA(new RuntimeException("Identifier not found: a"))
  }
  
  "Identifiers may not be redefined in the same scope" in {
  	val src = "let val a = 1 val a = 2 in a"
  	eval(src) must throwA(new RuntimeException("Duplicate identifier: a"))
  }
  
  "Identifiers may be redefined in nested scopes" in {
  	val src = "let val a = 1 in let val a = 2 in a"
  	eval(src) must_== 2
  }
}
