package csc424.relalg

trait Type {
  def matches(that: Type): Boolean
  def union(that: Type): Type
}

case object NullType extends Type {
  def matches(that: Type): Boolean = true
  def union(that: Type): Type = that
}

case object StringType extends Type {
  def matches(that: Type): Boolean = that match {
    case NullType => true
    case StringType => true
    case _ => false
  }
  
  def union(that: Type): Type = this
}

case object IntType extends Type {
  def matches(that: Type): Boolean = that match {
    case NullType => true
    case IntType => true
    case _ => false
  }
  
  def union(that: Type): Type = this
}