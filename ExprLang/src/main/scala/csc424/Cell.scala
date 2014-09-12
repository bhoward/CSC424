package csc424

import Common._

/**
 * A Cell acts as a storage place for a value. It is an abstraction
 * of a memory location.
 */
trait Cell {
  /**
   * Retrieve the value held in this Cell.
   */
  def value: ValueType

  /**
   * Change the value in this cell, if allowed.
   */
  def value_=(n: ValueType): Unit
}

/**
 * A ValCell is a read-only storage location. Attempting to modify the
 * value will throw an error.
 */
case class ValCell(value: ValueType) extends Cell {
  def value_=(n: ValueType) = sys.error("Attempt to change read-only value")
}

/**
 * A VarCell is a modifiable storage location. The value may be changed.
 */
case class VarCell(var value: ValueType) extends Cell
