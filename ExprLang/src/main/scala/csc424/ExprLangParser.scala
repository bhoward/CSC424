package csc424

import scala.util.parsing.combinator._

/**
 * Abstract syntax tree for an expression
 */
sealed trait Expr
case class DoExpr(stmts: List[Stmt], expr: Expr) extends Expr
case class LetExpr(decls: List[Decl], expr: Expr) extends Expr
case class BinOpExpr(op: String, left: Expr, right: Expr) extends Expr
case class UnOpExpr(op: String, expr: Expr) extends Expr
case class IdExpr(id: String) extends Expr
case class NumExpr(num: Int) extends Expr

/**
 * Abstract syntax tree for a statement
 */
sealed trait Stmt
case class AssignStmt(id: String, expr: Expr) extends Stmt
case class ReadStmt(id: String) extends Stmt
case class WriteStmt(expr: Expr) extends Stmt

/**
 * Abstract syntax tree for a declaration
 */
sealed trait Decl
case class ValDecl(id: String, expr: Expr) extends Decl
case class VarDecl(id: String, expr: Expr) extends Decl

/**
 * Combinator parser for the Expression Language.
 */
object ExprLangParser extends JavaTokenParsers with PackratParsers {
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
