package csc424.exprlang

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ParserTest extends Specification {
  "Simple expressions parse" in {
  	val src = "1 + 2 * 3"
  	val ast = BinOpExpr("+", NumExpr(1), BinOpExpr("*", NumExpr(2), NumExpr(3)))
  	Parser(src).get must_== ast
  }

  "Whitespace doesn't matter" in {
  	val src = """1+		2 // ignore me
  	            | *   /* and
                | me */  3""".stripMargin
  	val ast = BinOpExpr("+", NumExpr(1), BinOpExpr("*", NumExpr(2), NumExpr(3)))
  	Parser(src).get must_== ast
  }
  
  "Let expressions parse" in {
  	val src = "let val a = 1 val b = 2 val c = 3 in a + b * c"
  	val ast = LetExpr(List(ValDecl("a", NumExpr(1)),
                           ValDecl("b", NumExpr(2)),
                           ValDecl("c", NumExpr(3))),
                      BinOpExpr("+", IdExpr("a"),
                                     BinOpExpr("*", IdExpr("b"), IdExpr("c"))))
    Parser(src).get must_== ast
  }
  
//  "Simple expressions with floats parse" in {
//  	val src = "1 + 2.0 * 0.3e+1"
//  	val ast = BinOpExpr("+", NumExpr(1), BinOpExpr("*", NumExpr(2), NumExpr(3)))
//  	ExprLangParser(src).get must_== ast
//  }
//  
//  "Unary functions are parsed" in {
//  	val src = "sqrt(2) + log(1)"
//  	val ast = BinOpExpr("+", UnOpExpr("sqrt", NumExpr(2)), UnOpExpr("log", NumExpr(1)))
//  	ExprLangParser(src).get must_== ast
//  }
//  
//  "Binary functions are parsed" in {
//  	val src = "max(79, 37) - min(79, 37)"
//  	val ast = BinOpExpr("-", BinOpExpr("max", NumExpr(79), NumExpr(37)),
//  	                         BinOpExpr("min", NumExpr(79), NumExpr(37)))
//  	ExprLangParser(src).get must_== ast
//  }

  "Binary operators require two operands" in {
  	val src = "2 +"
  	Parser(src).successful must_== false
  }
  
  "Let expressions must have at least one binding" in {
  	val src = "let in 1"
  	Parser(src).successful must_== false
  }
  
  "Let expressions require val binding keyword" in {
  	val src = "let x = 1 in x"
  	Parser(src).successful must_== false
  }
}
