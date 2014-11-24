package csc424.html

trait Item {
  def minWidth: Int
  def minHeight: Int
  def render(width: Int, height: Int): List[String]
}

case class Text(text: String) extends Item {
  val minWidth: Int = text.length
  val minHeight: Int = 1
  def render(width: Int, height: Int): List[String] =
    (text + " " * (width - text.length)) ::
      List.fill(height - 1)(" " * width)
}

case class Table(rows: List[Row]) extends Item {
  val colWidths: List[Int] = (rows map { _.colWidths }).
    foldRight(List[Int]())((ws1, ws2) =>
      ws1.zipAll(ws2, 0, 0) map { case (w1, w2) => w1 max w2 })
  val minWidth: Int = colWidths.sum
  val minHeight: Int = (rows map { _.minHeight }).sum
  def render(width: Int, height: Int): List[String] = {
    rows.foldRight(List[String]())((r, ss) =>
      r.render(width, r.minHeight, colWidths) ++ ss)
  }
  def render(): List[String] = render(minWidth, minHeight)
}

case class Row(cells: List[Cell]) {
  val colWidths: List[Int] = cells map { _.minWidth }
  val minHeight: Int = (0 :: (cells map { _.minHeight })).max
  def render(width: Int, height: Int, colWidths: List[Int]): List[String] = {
    ((cells.zipAll(colWidths, Cell(Text("")), 0)) map { case (c, w) => c.render(w, height) }).
      foldRight(List.fill(height)(""))((ss1, ss2) =>
        (ss1 zip ss2) map { case (s1, s2) => s1 + s2 })
  }
}

case class Cell(item: Item) {
  val minWidth: Int = item.minWidth
  val minHeight: Int = item.minHeight
  def render(width: Int, height: Int): List[String] =
    item.render(width, height)
}