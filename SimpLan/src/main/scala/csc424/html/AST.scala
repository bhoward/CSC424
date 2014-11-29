package csc424.html

/**
 * Represents one cell of an HTML table.
 */
trait Cell {
  /**
   * Calculate the minimum required width (in characters) for this item.
   */
  def minWidth: Int

  /**
   * Calculate the minimum required height (in lines) for this item.
   */
  def minHeight: Int

  /**
   * Render this item to the given width and height.
   *
   * @param width in characters
   * @param height in lines
   * @return a list of height strings, each width characters long
   */
  def render(width: Int, height: Int): List[String]
}

/**
 * Represents a cell that is just a single line of text.
 */
case class Text(text: String) extends Cell {
  val minWidth: Int = text.length + 2 // allow 1 character padding on each side

  val minHeight: Int = 1

  def render(width: Int, height: Int): List[String] = {
    val padLeft = (width - text.length) / 2
    val padRight = width - text.length - padLeft
    val padTop = (height - 1) / 2
    val padBottom = height - 1 - padTop

    List.fill(padTop)(" " * width) :::
      List(" " * padLeft + text + " " * padRight) :::
      List.fill(padBottom)(" " * width)
  }
}

/**
 * Represents an entire HTML table (which may be a cell embedded in a larger table).
 */
case class Table(rows: List[Row]) extends Cell {
  /**
   * A list of the required widths of the columns in this table.
   */
  val colWidths: List[Int] = (rows map { _.colWidths }).
    foldRight(List[Int]())((ws1, ws2) =>
      ws1.zipAll(ws2, 0, 0) map { case (w1, w2) => w1 max w2 })

  val minWidth: Int = colWidths.sum

  val minHeight: Int = (rows map { _.minHeight }).sum

  def render(width: Int, height: Int): List[String] = {
    val padLeft = (width - minWidth) / 2
    val padRight = width - minWidth - padLeft
    val padTop = (height - minHeight) / 2
    val padBottom = height - minHeight - padTop

    List.fill(padTop)(" " * width) :::
      (rows.foldRight(List[String]())((r, ss) =>
        r.render(r.minHeight, colWidths) ++ ss) map
        { " " * padLeft + _ + " " * padRight }) :::
      List.fill(padBottom)(" " * width)
  }

  /**
   * Render this table to its minimum required size.
   *
   * @return a list of equal-length strings
   */
  def render: List[String] = render(minWidth, minHeight)
}

/**
 * Represents one row of an HTML table.
 */
case class Row(cells: List[Cell]) {
  /**
   * A list of the required widths of the cells of this row.
   */
  val colWidths: List[Int] = cells map { _.minWidth }

  /**
   * The minimum required height for this row.
   */
  val minHeight: Int = (1 :: (cells map { _.minHeight })).max

  /**
   * Render this row to the given height, using the provided list of
   * column widths to align each cell.
   *
   * @param height in lines
   * @param colWidths a list of desired column widths
   */
  def render(height: Int, colWidths: List[Int]): List[String] = {
    ((cells.zipAll(colWidths, Text(""), 0)) map { case (c, w) => c.render(w, height) }).
      foldRight(List.fill(height)(""))((ss1, ss2) =>
        (ss1 zip ss2) map { case (s1, s2) => s1 + s2 })
  }
}
