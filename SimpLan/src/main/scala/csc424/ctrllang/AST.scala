package csc424.ctrllang

/**
 * Abstract syntax tree for an expression
 */
sealed trait Expr
case class DoExpr(stmts: List[Stmt], expr: Expr) extends Expr
case class LetExpr(decls: List[Decl], expr: Expr) extends Expr
case class CondExpr(test: BoolExpr, trueExpr: Expr, falseExpr: Expr) extends Expr
case class BinOpExpr(op: String, left: Expr, right: Expr) extends Expr
case class UnOpExpr(op: String, expr: Expr) extends Expr
case class IdExpr(id: String) extends Expr
case class NumExpr(num: Double) extends Expr

/**
 * Abstract syntax tree for a boolean expression
 */
sealed trait BoolExpr
case class RelBExpr(op: String, left: Expr, right: Expr) extends BoolExpr
case class ConstBExpr(const: Boolean) extends BoolExpr
case class AndBExpr(left: BoolExpr, right: BoolExpr) extends BoolExpr
case class OrBExpr(left: BoolExpr, right: BoolExpr) extends BoolExpr
case class NotBExpr(bexpr: BoolExpr) extends BoolExpr

/**
 * Abstract syntax tree for a statement
 */
sealed trait Stmt
case class AssignStmt(id: String, expr: Expr) extends Stmt
case class ReadStmt(id: String) extends Stmt
case class WriteStmt(expr: Expr) extends Stmt
case class PromptReadStmt(prompt: String, id: String) extends Stmt
case class StringWriteStmt(message: String) extends Stmt
case class SwapStmt(id1: String, id2: String) extends Stmt
case class SwapIfStmt(id1: String, id2: String) extends Stmt
case class IfThenElseStmt(test: BoolExpr, trueClause: List[Stmt], falseClause: List[Stmt]) extends Stmt
case class IfThenStmt(test: BoolExpr, trueClause: List[Stmt]) extends Stmt
case class WhileStmt(test: BoolExpr, body: List[Stmt]) extends Stmt

/**
 * Abstract syntax tree for a declaration
 */
sealed trait Decl
case class ValDecl(id: String, expr: Expr) extends Decl
case class VarDecl(id: String, expr: Expr) extends Decl
