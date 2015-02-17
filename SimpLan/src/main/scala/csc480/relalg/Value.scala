package csc480.relalg

import scala.language.implicitConversions

trait Value extends Expression {
  def apply(schema: Schema)(row: Row): Value = this
  
  def compareTo(that: Value): Int
  
  def asInt: Int
  
  def isNull: Boolean = false
}

object Value {
  implicit def string2Value(s: String): Value = new StringValue(s)
  implicit def int2Value(n: Int): Value = new IntValue(n)
  
  implicit val valueOrdering = new Ordering[Value] {
    def compare(v1: Value, v2: Value): Int = v1.compareTo(v2)
  }
}

case object NullValue extends Value {
  def typeGiven(schema: Schema): Type = NullType
  
  override def toString: String = "NULL"
    
  def compareTo(that: Value): Int = -1  // everything follows NULL, even other NULLs
  
  def asInt: Int = 0
  
  override def isNull: Boolean = true
}

case class StringValue(s: String) extends Value {
  def typeGiven(schema: Schema): Type = StringType
  
  override def toString: String = s
  
  def compareTo(that: Value): Int = that match {
    case NullValue => 1
    case StringValue(t) => s.compareTo(t)
    case IntValue(m) => s.compareTo(m.toString)
  }
  
  def asInt: Int = 0
}

case class IntValue(n: Int) extends Value {
  def typeGiven(schema: Schema): Type = IntType
  
  override def toString: String = n.toString
  
  def compareTo(that: Value): Int = that match {
    case NullValue => 1
    case StringValue(t) => n.toString.compareTo(t)
    case IntValue(m) => n.compareTo(m)
  }
  
  def asInt: Int = n
}
