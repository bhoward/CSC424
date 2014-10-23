package csc424.ctrllang

import Language._

/**
 * A Cell acts as a storage place for a value. It is an abstraction
 * of a memory location.
 */
trait Cell[T] {
  /**
   * Retrieve the value held in this Cell.
   */
  def value: T

  /**
   * Change the value in this cell, if allowed.
   */
  def value_=(n: T): Unit
}

/**
 * A ValCell is a read-only storage location. Attempting to modify the
 * value will throw an error.
 */
case class ValCell[T](value: T) extends Cell[T] {
  def value_=(n: T) = sys.error("Attempt to change read-only value")
}

/**
 * A VarCell is a modifiable storage location. The value may be changed.
 */
case class VarCell[T](var value: T) extends Cell[T]
