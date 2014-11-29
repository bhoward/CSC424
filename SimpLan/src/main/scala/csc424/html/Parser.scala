package csc424.html

import scala.util.parsing.combinator._
import java.io.Reader

/**
 * Combinator parser for the HTML Tables project.
 */
object Parser extends RegexParsers with PackratParsers {
  type P[T] = PackratParser[T]

  lazy val table: P[Table] =
    ("<TABLE>" ~ rep(row) ~ "</TABLE>" ^^ { case _ ~ rows ~ _ => Table(rows) })

  lazy val row: P[Row] =
    ("<TR>" ~ rep(cell) ~ "</TR>" ^^ { case _ ~ cells ~ _ => Row(cells) })

  lazy val cell: P[Cell] =
    ("<TD>" ~ item ~ "</TD>" ^^ { case _ ~ it ~ _ => it })

  lazy val item: P[Cell] =
    (table
      | text ^^ { case t => Text(t) })

  val text: Parser[String] = """[^<]*""".r

  def apply(in: String) = parseAll(table, in)

  def parse(in: Reader): Table = parseAll(table, in) match {
    case Success(result, _) => result
    case _ => sys.error("unable to parse table")
  }
}