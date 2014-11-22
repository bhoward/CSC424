package csc424.lambda

import scala.util.parsing.combinator._
import java.io.Reader
import csc424.simplide.InterpreterException

object Parser extends RegexParsers with PackratParsers {
  type P[T] = PackratParser[T]
  
  // A program may start with a list of definitions;
  // this is merely syntactic sugar for bindings with lambda
  lazy val prog: P[Expr] =
  ( ident ~ "=" ~ expr ~ ";" ~ prog ^^
      {case id ~ _ ~ e ~ _ ~ p => AppExpr(FunExpr(id, p), e)}
  | expr
  )
  
  lazy val expr: P[Expr] =
  ( expr ~ term ^^ {case fun ~ arg => AppExpr(fun, arg)}
  | term
  )

  lazy val term: P[Expr] =
  ( ident ~ "=>" ~ expr ^^ {case param ~ _ ~ body => FunExpr(param, body)}
  | "(" ~ expr ~ ")"    ^^ {case _ ~ e ~ _ => e}
  | ident               ^^ {case id => IdExpr(id)}
  | number              ^^ {case n => churchNumeral(n.toInt)}
  )
  
  val ident: Parser[String] = """[A-Za-z][A-Za-z0-9]*""".r
     
  val number: Parser[String] = """[1-9][0-9]*|0""".r

  // As a convenience, integers will be parsed into Church numerals
  def churchNumeral(n: Int) =
    FunExpr("s", FunExpr("z",
      iterate(n, (e: Expr) => AppExpr(IdExpr("s"), e), IdExpr("z"))))
  
  def iterate[T](n: Int, f: T => T, a: T): T =
    if (n == 0) a
    else iterate(n - 1, f, f(a))

  override val whiteSpace = """(\s|//.*|/\*(\*(?!/)|[^*])*\*/)+""".r

  def apply(in: String) = parseAll(prog, in)
  
  def parse(in: Reader): Expr = parseAll(prog, in) match {
    case Success(result, _) => result
    case ns: NoSuccess => throw new InterpreterException(ns.msg)
  }
}
