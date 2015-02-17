package csc480.relalg

trait Row extends IndexedSeq[Value] {
  def project(indices: Iterable[Int]): Row
  def extend(schema: Schema, value: Expression): Row
  def product(that: Row): Row
}

class DefaultRow(values: IndexedSeq[Value]) extends Row {
  def length: Int = values.length
  def apply(i: Int): Value = values(i)
  
  override def toString: String = values.mkString(", ")
  
  def project(indices: Iterable[Int]): Row = {
    val values2 = for (i <- indices) yield if (i >= 0) values(i) else NullValue
    new DefaultRow(values2.toIndexedSeq)
  }
  
  def extend(schema: Schema, value: Expression): Row =
    new DefaultRow(values :+ value(schema)(this))
  
  def product(that: Row): Row =
    new DefaultRow(values ++ that)
}

object Row {
  def apply(values: Value*): Row = new DefaultRow(values.toIndexedSeq)
}
