package csc480.relalg

import scala.math.Ordering

class LexicalRowOrdering(indices: Iterable[Int]) extends Ordering[Row] {
  def compare(row1: Row, row2: Row): Int = {
    for (i <- indices) {
      val comp = row1(i) compareTo row2(i)
      if (comp != 0) return comp
    }
    0
  }
}
