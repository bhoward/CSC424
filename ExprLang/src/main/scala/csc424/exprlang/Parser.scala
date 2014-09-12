package csc424.exprlang

import scala.util.parsing.combinator._

/**
 * Combinator parser for the Expression Language.
 */
object Parser extends JavaTokenParsers with PackratParsers {
  lazy val expr: PackratParser[Expr] =
  ( "do" ~ rep1(stmt) ~ "in" ~ expr  ^^ {case _ ~ ss ~ _ ~ e => DoExpr(ss, e)}
  | "let" ~ rep1(decl) ~ "in" ~ expr ^^ {case _ ~ ds ~ _ ~ e => LetExpr(ds, e)}
  | expr ~ addop ~ term              ^^ {case e ~ op ~ t => BinOpExpr(op, e, t)}
  | term
  )

  lazy val term: PackratParser[Expr] =
  ( term ~ mulop ~ factor ^^ {case t ~ op ~ f => BinOpExpr(op, t, f)}
  | factor
  )

  lazy val factor: PackratParser[Expr] =
  ( "-" ~ factor     ^^ {case op ~ f => UnOpExpr(op, f)}
  | "(" ~ expr ~ ")" ^^ {case _ ~ e ~ _ => e}
  | ident            ^^ {case id => IdExpr(id)}
  | wholeNumber      ^^ {case numLit => NumExpr(numLit.toInt)}
  )
  
  lazy val stmt: PackratParser[Stmt] =
  ( ident ~ "=" ~ expr ^^ {case id ~ _ ~ e => AssignStmt(id, e)}
  | "read" ~ ident     ^^ {case _ ~ id => ReadStmt(id)}
  | "write" ~ expr     ^^ {case _ ~ e => WriteStmt(e)}
  )
  
  lazy val decl: PackratParser[Decl] =
  ( "val" ~ ident ~ "=" ~ expr ^^ {case _ ~ id ~ _ ~ e => ValDecl(id, e)}
  | "var" ~ ident ~ "=" ~ expr ^^ {case _ ~ id ~ _ ~ e => VarDecl(id, e)}
  )

  lazy val addop: PackratParser[String] = "+" | "-"

  lazy val mulop: PackratParser[String] = "*" | "/" | "%"

  def apply(in: String) = parseAll(expr, in)
}
