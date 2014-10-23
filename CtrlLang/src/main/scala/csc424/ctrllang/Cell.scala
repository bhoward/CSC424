package csc424.ctrllang

import Language._

/**
 * A Cell acts as a storage place for a value. It is an abstraction
 * of a memory location.
 */
trait Cell {
  /**
   * Retrieve the value held in this Cell.
   */
  def value: Result

  /**
   * Change the value in this cell, if allowed.
   */
  def value_=(n: Result): Unit
}

/**
 * A ValCell is a read-only storage location. Attempting to modify the
 * value will throw an error.
 */
case class ValCell(value: Result) extends Cell {
  def value_=(n: Result) = sys.error("Attempt to change read-only value")
}

/**
 * A VarCell is a modifiable storage location. The value may be changed.
 */
case class VarCell(var value: Result) extends Cell
