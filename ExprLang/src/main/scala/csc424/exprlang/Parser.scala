package csc424.exprlang

import scala.util.parsing.combinator._

/**
 * Combinator parser for the Expression Language.
 */
object Parser extends RegexParsers with PackratParsers {
  type P[T] = PackratParser[T]
  
  lazy val expr: P[Expr] =
  ( "do" ~ rep1(stmt) ~ "in" ~ expr  ^^ {case _ ~ ss ~ _ ~ e => DoExpr(ss, e)}
  | "let" ~ rep1(decl) ~ "in" ~ expr ^^ {case _ ~ ds ~ _ ~ e => LetExpr(ds, e)}
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
  
  lazy val stmt: P[Stmt] =
  ( ident ~ "=" ~ expr ^^ {case id ~ _ ~ e => AssignStmt(id, e)}
  | "read" ~ ident     ^^ {case _ ~ id => ReadStmt(id)}
  | "write" ~ expr     ^^ {case _ ~ e => WriteStmt(e)}
  )
  
  lazy val decl: P[Decl] =
  ( "val" ~ ident ~ "=" ~ expr ^^ {case _ ~ id ~ _ ~ e => ValDecl(id, e)}
  | "var" ~ ident ~ "=" ~ expr ^^ {case _ ~ id ~ _ ~ e => VarDecl(id, e)}
  )

  val addop: Parser[String] = "+" | "-"

  val mulop: Parser[String] = "*" | "/" | "%"
  
  val ident: Parser[String] = """[A-Za-z][A-Za-z0-9]*""".r
     
  val wholeNumber: Parser[String] = """[1-9][0-9]*|0""".r
  
  override val whiteSpace = """(\s|//.*|/\*(\*(?!/)|[^*])*\*/)+""".r

  def apply(in: String) = parseAll(expr, in)
}
