package csc424

import Common._

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
