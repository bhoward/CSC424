package csc424

import java.util.Scanner

/**
 * The package object holds useful definitions used by the entire package.
 */
package object ctrllang {
  /**
   * ValueType is a synonym for the type of values that may be stored in Cells.
   */
  type ValueType = Double
  
  private val decimalFormat = new java.text.DecimalFormat("0.##########")
  def show(value: ValueType): String = decimalFormat.format(value)
  
  def read(in: Scanner): ValueType = in.nextDouble
}