package csc424

import java.util.Scanner

/**
 * The package object holds useful definitions used by the entire package.
 */
package object exprlang {
  /**
   * ValueType is a synonym for the type of values that may be stored in Cells.
   */
  type ValueType = Int
  
  def show(value: ValueType): String = value.toString
  
  def read(in: Scanner): ValueType = in.nextInt

}