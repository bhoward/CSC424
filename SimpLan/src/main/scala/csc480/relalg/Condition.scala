package csc480.relalg

trait Condition {
  def apply(schema: Schema)(row: Row): Boolean
}

case class OpCondition(expr1: Expression, expr2: Expression, op: Int => Boolean) extends Condition {
  def apply(schema: Schema)(row: Row): Boolean =
    op(expr1(schema)(row) compareTo expr2(schema)(row))
}

case class AndCondition(cond1: Condition, cond2: Condition) extends Condition {
  def apply(schema: Schema)(row: Row): Boolean =
    cond1(schema)(row) && cond2(schema)(row)
}

case class OrCondition(cond1: Condition, cond2: Condition) extends Condition {
  def apply(schema: Schema)(row: Row): Boolean =
    cond1(schema)(row) || cond2(schema)(row)
}

case class NotCondition(cond: Condition) extends Condition {
  def apply(schema: Schema)(row: Row): Boolean =
    !cond(schema)(row)
}