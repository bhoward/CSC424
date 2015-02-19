package csc480.relalg

import scala.language.implicitConversions

trait Expression {
  def apply(schema: Schema)(row: Row): Value
  
  def typeGiven(schema: Schema): Type
  
  def +(that: Expression): Expression = new IntOpExpression(this, that, _ + _)
  def -(that: Expression): Expression = new IntOpExpression(this, that, _ - _)
  def *(that: Expression): Expression = new IntOpExpression(this, that, _ * _)
  def /(that: Expression): Expression = new IntOpExpression(this, that, _ / _)
  def %(that: Expression): Expression = new IntOpExpression(this, that, _ % _)
  def unary_- : Expression = new UnaryIntOpExpression(this, -_)
  
  def ==(that: Expression): Condition = new OpCondition(this, that, _ == 0)
  def !=(that: Expression): Condition = new OpCondition(this, that, _ != 0)
  def <(that: Expression): Condition = new OpCondition(this, that, _ < 0)
  def >(that: Expression): Condition = new OpCondition(this, that, _ > 0)
  def <=(that: Expression): Condition = new OpCondition(this, that, _ <= 0)
  def >=(that: Expression): Condition = new OpCondition(this, that, _ >= 0)
}

object Expression {
  implicit def string2Expression(s: String): Expression = new StringValue(s)
  implicit def int2Expression(n: Int): Expression = new IntValue(n)
}

case class F(name: String) extends Expression {
  def apply(schema: Schema)(row: Row): Value = schema(name)(row)
  
  def typeGiven(schema: Schema): Type = schema.typeOf(name)
}

case class IntOpExpression(expr1: Expression, expr2: Expression, op: (Int, Int) => Int) extends Expression {
  def apply(schema: Schema)(row: Row): Value = {
    (expr1(schema)(row), expr2(schema)(row)) match {
      case (IntValue(m), IntValue(n)) => IntValue(op(m, n))
      case _ => NullValue
    }
  }
  
  def typeGiven(schema: Schema): Type = IntType
}

case class UnaryIntOpExpression(expr: Expression, op: Int => Int) extends Expression {
  def apply(schema: Schema)(row: Row): Value = {
    expr(schema)(row) match {
      case IntValue(n) => IntValue(op(n))
      case _ => NullValue
    }
  }
  
  def typeGiven(schema: Schema): Type = IntType
}
