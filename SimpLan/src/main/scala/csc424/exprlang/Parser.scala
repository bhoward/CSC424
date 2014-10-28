package csc424.exprlang

import scala.util.parsing.combinator._
import java.io.Reader
import csc424.simplide.InterpreterException

/**
 * Combinator parser for the Expression Language.
 */
object Parser extends RegexParsers with PackratParsers {
  type P[T] = PackratParser[T]
  
  lazy val expr: P[Expr] =
  ( "let" ~ rep1(decl) ~ "in" ~ expr ^^ {case _ ~ ds ~ _ ~ e => LetExpr(ds, e)}
  | "if" ~ bexpr ~
    "then" ~ expr ~ "else" ~ expr    ^^ {case _ ~ be ~ _ ~ e1 ~ _ ~ e2 => CondExpr(be, e1, e2)}
  | expr ~ addop ~ term              ^^ {case e ~ op ~ t => BinOpExpr(op, e, t)}
  | term
  )

  lazy val term: P[Expr] =
  ( term ~ mulop ~ factor ^^ {case t ~ op ~ f => BinOpExpr(op, t, f)}
  | factor
  )

  lazy val factor: P[Expr] =
  ( "-" ~ factor     ^^ {case op ~ f => UnOpExpr(op, f)}
  | "(" ~ expr ~ ")" ^^ {case _ ~ e ~ _ => e}
  | ident            ^^ {case id => IdExpr(id)}
  | wholeNumber      ^^ {case numLit => NumExpr(numLit.toInt)}
  )
  
  lazy val bexpr: P[BoolExpr] =
  ( bexpr ~ "||" ~ bterm ^^ {case e1 ~ _ ~ e2 => OrBExpr(e1, e2)}
  | bterm
  )
  
  lazy val bterm: P[BoolExpr] =
  ( bterm ~ "&&" ~ bfactor ^^ {case e1 ~ _ ~ e2 => AndBExpr(e1, e2)}
  | bfactor
  )
  
  lazy val bfactor: P[BoolExpr] =
  ( "!" ~ bfactor       ^^ {case _ ~ f => NotBExpr(f)}
  | "(" ~ bexpr ~ ")"   ^^ {case _ ~ e ~ _ => e}
  | expr ~ relop ~ expr ^^ {case e1 ~ op ~ e2 => RelBExpr(op, e1, e2)}
  | "true"              ^^ {case _ => ConstBExpr(true)}
  | "false"             ^^ {case _ => ConstBExpr(false)}
  )
  
  lazy val decl: P[Decl] =
  ( "val" ~ ident ~ "=" ~ expr ^^ {case _ ~ id ~ _ ~ e => ValDecl(id, e)}
  )

  val addop: Parser[String] = "+" | "-"

  val mulop: Parser[String] = "*" | "/" | "%"
  
  val relop: Parser[String] = "==" | "!=" | "<" | ">" | "<=" | ">="
  
  val ident: Parser[String] = """[A-Za-z][A-Za-z0-9]*""".r
     
  val wholeNumber: Parser[String] = """[1-9][0-9]*|0""".r
  
  override val whiteSpace = """(\s|//.*|/\*(\*(?!/)|[^*])*\*/)+""".r

  def apply(in: String) = parseAll(expr, in)
  
  def parse(in: Reader): Expr = parseAll(expr, in) match {
    case Success(result, _) => result
    case ns: NoSuccess => throw new InterpreterException(ns.msg)
  }
}
