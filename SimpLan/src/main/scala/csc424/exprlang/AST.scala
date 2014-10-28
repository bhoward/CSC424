package csc424.exprlang

/**
 * Abstract syntax tree for an expression
 */
sealed trait Expr
case class LetExpr(decls: List[Decl], expr: Expr) extends Expr
case class CondExpr(test: BoolExpr, trueExpr: Expr, falseExpr: Expr) extends Expr
case class BinOpExpr(op: String, left: Expr, right: Expr) extends Expr
case class UnOpExpr(op: String, expr: Expr) extends Expr
case class IdExpr(id: String) extends Expr
case class NumExpr(num: Int) extends Expr

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
 * Abstract syntax tree for a declaration
 */
sealed trait Decl
case class ValDecl(id: String, expr: Expr) extends Decl
