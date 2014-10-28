package csc424.ctrllang

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

  "Do expressions allow modifying vars" in {
    val src = """let
                |  var a = 1
                |  var b = 0
                |in do
                |  b = a
                |  a = 2
                |in a + b""".stripMargin
    eval(src) must_== 3
  }

  "Read statements get input" in {
  	val src = "let var x = 0 in do read x in x"
  	withInput("42") {
  	  withOutput("x? ") {
  	  	eval(src)
  	  }
  	} must_== 42
  }

  "Write statements produce output" in {
    val src = "do write 42 in 0"
    withOutput("42", "") {
      eval(src)
    } must_== 0
  }
  
  "Several values can be read" in {
    val src = """let
                |  var x = 0
                |  var y = 0
                |in do
                |  read x
                |  read y
                |in x * y""".stripMargin
    withInput("6 7") {
      withOutput("x? y? ") {
        eval(src)
      }
    } must_== 42
  }
  
  "String write statements produce output" in {
    val src = """do write "Hello World!" in 0"""
    withOutput("Hello World!", "") {
      eval(src)
    } must_== 0
  }

  "Simple expressions with floats can be evaluated" in {
    eval("1+2.0*0.3e+1") must_== 7
  }

  "Floating point values work" in {
  	val src = "1.2 * 3 - 26e-1"
  	eval(src) must be closeTo(1.0 +/- 0.0001)
  }

  "Unary functions are evaluated" in {
  	val src = "sqrt(2) + log(1)"
  	eval(src) must be closeTo(1.4142 +/- 0.0001)
  }
  
  "Binary functions are evaluated" in {
  	val src = "max(79, 37) - min(79, 37)"
  	eval(src) must_== 42
  }
  
  "String literals may be used in I/O statements" in {
  	val src = """let
  	            |  var x = 0
  	            |in do
  	            |  read "Enter a number: ", x
  	            |  x = x * 2
  	            |  write "Your number doubled is"
  	            |  write x
  	            |in x""".stripMargin
    withInput("3.14") {
      withOutput("Enter a number: Your number doubled is", "6.28", "") {
        eval(src)
      }
    } must_== 6.28
  }

  "swap statements are executed" in {
  	val src = "let var first = 1 var second = 2 in do swap first, second in first - second"
  	eval(src) must_== 1
  }
  
  "swapif statements are executed, I" in {
    val src = "let var first = 1 var second = 2 in do swapif first, second in first - second"
    eval(src) must_== -1
  }
  
  "swapif statements are executed, II" in {
    val src = "let var first = 2 var second = 1 in do swapif first, second in first - second"
    eval(src) must_== -1
  }
  
  "If statements are executed, I" in {
    val src = """do
                |  if true
                |  then write "OK"
                |  else write "No"
                |  endif
                |in 0""".stripMargin
    withOutput("OK", "") {
      eval(src)
    } must_== 0
  }

  "If statements are executed, II" in {
    val src = """do
                |  if true
                |  then write "OK"
                |    write "No"
                |  endif
                |in 0""".stripMargin
    withOutput("OK", "No", "") {
      eval(src)
    } must_== 0
  }

  "While statements are executed" in {
    val src = """let
                |  var x = 3
                |in do
                |  while x > 0 begin
                |    write x
                |    x = x - 1
                |  end
                |in x""".stripMargin
    withOutput("3", "2", "1", "") {
      eval(src)
    } must_== 0
  }

  "Undeclared identifiers are caught" in {
  	val src = "a + b * c"
  	eval(src) must throwA(new RuntimeException("Identifier not found: a"))
  }
  
  "Values are read-only" in {
  	val src = "let val x = 42 in do x = 17 in x"
  	eval(src) must throwA(new RuntimeException("Attempt to change read-only value"))
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
