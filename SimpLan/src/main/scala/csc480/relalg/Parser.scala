package csc480.relalg

import scala.util.parsing.combinator._
import java.io.Reader
import csc424.simplide.InterpreterException

/**
 * Combinator parser for the Relational Algebra Language.
 */
object Parser extends RegexParsers with PackratParsers {
  type P[T] = PackratParser[T]
  
  lazy val cmds: P[List[Command]] = rep(cmd)
  
  lazy val cmd: P[Command] =
  ( "create" ~ ident ~ "(" ~ rep1sep(col, ",") ~ ")" ~ "(" ~ rep1sep(row, ",") ~ ")" ^^
      {case _ ~ id ~ _ ~ schema ~ _ ~ _ ~ data ~ _ => CreateCommand(id, schema, data)}
  | ident ~ "=" ~ texpr ^^
      {case id ~ _ ~ te => QueryCommand(id, te)}
  )
  
  lazy val col: P[(String, Type)] =
  ( ident ~ "int" ^^ {case id ~ _ => (id, IntType)}
  | ident ~ "string" ^^ {case id ~ _ => (id, StringType)}
  )
  
  lazy val row: P[List[Value]] =
  ( "(" ~ rep1sep(value, ",") ~ ")" ^^ {case _ ~ data ~ _ => data}
  )
  
  lazy val value: P[Value] =
  ( number           ^^ {case n => IntValue(n.toInt)}
  | stringLiteral    ^^ {case s => StringValue(unquote(s))}
  )
  
  lazy val texpr: P[TExpr] =
  ( "select" ~ "(" ~ texpr ~ "," ~ cond ~ ")" ^^
      {case _ ~ _ ~ te ~ _ ~ c ~ _ => SelectTExpr(te, c)}
  | "project" ~ "(" ~ texpr ~ "," ~ "{" ~ rep1sep(ident, ",") ~ "}" ~ ")" ^^
      {case _ ~ _ ~ te ~ _ ~ _ ~ fs ~ _ ~ _ => ProjectTExpr(te, fs)}
  | "sort" ~ "(" ~ texpr ~ "," ~ "[" ~ rep1sep(ident, ",") ~ "]" ~ ")" ^^
      {case _ ~ _ ~ te ~ _ ~ _ ~ fs ~ _ ~ _ => SortTExpr(te, fs)}
  | "rename" ~ "(" ~ texpr ~ "," ~ ident ~ "," ~ ident ~ ")" ^^
      {case _ ~ _ ~ te ~ _ ~ on ~ _ ~ nn ~ _ => RenameTExpr(te, on, nn)}
  | "extend" ~ "(" ~ texpr ~ "," ~ expr ~ "," ~ ident ~ ")" ^^
      {case _ ~ _ ~ te ~ _ ~ ex ~ _ ~ f ~ _ => ExtendTExpr(te, ex, f)}
  | "groupby" ~ "(" ~ texpr ~ "," ~ "{" ~ repsep(ident, ",") ~ "}" ~ "," ~ "{" ~ repsep(agg, ",") ~ "}" ~ ")" ^^
      {case _ ~ _ ~ te ~ _ ~ _ ~ fs ~ _ ~ _ ~ _ ~ aggs ~ _ ~ _ => GroupByTExpr(te, fs, aggs)}
  | "product" ~ "(" ~ texpr ~ "," ~ texpr ~ ")" ^^
      {case _ ~ _ ~ te1 ~ _ ~ te2 ~ _ => ProductTExpr(te1, te2)}
  | "join" ~ "(" ~ texpr ~ "," ~ texpr ~ "," ~ cond ~ ")" ^^
      {case _ ~ _ ~ te1 ~ _ ~ te2 ~ _ ~ c ~ _ => JoinTExpr(te1, te2, c)}
  | "semijoin" ~ "(" ~ texpr ~ "," ~ texpr ~ "," ~ cond ~ ")" ^^
      {case _ ~ _ ~ te1 ~ _ ~ te2 ~ _ ~ c ~ _ => SemiJoinTExpr(te1, te2, c)}
  | "antijoin" ~ "(" ~ texpr ~ "," ~ texpr ~ "," ~ cond ~ ")" ^^
      {case _ ~ _ ~ te1 ~ _ ~ te2 ~ _ ~ c ~ _ => AntiJoinTExpr(te1, te2, c)}
  | "outerjoin" ~ "(" ~ texpr ~ "," ~ texpr ~ "," ~ cond ~ ")" ^^
      {case _ ~ _ ~ te1 ~ _ ~ te2 ~ _ ~ c ~ _ => OuterJoinTExpr(te1, te2, c)}
  | "union" ~ "(" ~ texpr ~ "," ~ texpr ~ ")" ^^
      {case _ ~ _ ~ te1 ~ _ ~ te2 ~ _ => UnionTExpr(te1, te2)}
  | ident ^^
      {case id => IdTExpr(id)}
  )
  
  lazy val cond: P[Condition] =
  ( cond ~ "and" ~ opcond ^^ {case c1 ~ _ ~ c2 => AndCondition(c1, c2)}
  | cond ~ "or" ~ opcond ^^ {case c1 ~ _ ~ c2 => OrCondition(c1, c2)}
  | opcond
  )
  
  lazy val opcond: P[Condition] =
  ( expr ~ "=" ~ expr ^^ {case e1 ~ _ ~ e2 => e1 == e2}
  | expr ~ "<>" ~ expr ^^ {case e1 ~ _ ~ e2 => e1 != e2}
  | expr ~ "<" ~ expr ^^ {case e1 ~ _ ~ e2 => e1 < e2}
  | expr ~ ">" ~ expr ^^ {case e1 ~ _ ~ e2 => e1 > e2}
  | expr ~ "<=" ~ expr ^^ {case e1 ~ _ ~ e2 => e1 <= e2}
  | expr ~ ">=" ~ expr ^^ {case e1 ~ _ ~ e2 => e1 >= e2}
  )
  
  lazy val expr: P[Expression] =
  ( expr ~ "+" ~ term ^^ {case e ~ _ ~ t => e + t}
  | expr ~ "-" ~ term ^^ {case e ~ _ ~ t => e - t}
  | term
  )
  
  lazy val term: P[Expression] =
  ( term ~ "*" ~ factor ^^ {case t ~ _ ~ f => t * f}
  | term ~ "/" ~ factor ^^ {case t ~ _ ~ f => t / f}
  | term ~ "%" ~ factor ^^ {case t ~ _ ~ f => t % f}
  | factor
  )
  
  lazy val factor: P[Expression] =
  ( "(" ~ expr ~ ")" ^^ {case _ ~ e ~ _ => e}
  | ident            ^^ {case id => F(id)}
  | value
  )
  
  lazy val agg: P[Aggregation] =
  ( "Min" ~ "(" ~ ident ~ ")" ^^ {case _ ~ _ ~ f ~ _ => MinAggregation(f)}
  | "Max" ~ "(" ~ ident ~ ")" ^^ {case _ ~ _ ~ f ~ _ => MaxAggregation(f)}
  | "Sum" ~ "(" ~ ident ~ ")" ^^ {case _ ~ _ ~ f ~ _ => SumAggregation(f)}
  | "Avg" ~ "(" ~ ident ~ ")" ^^ {case _ ~ _ ~ f ~ _ => AvgAggregation(f)}
  | "Count" ~ "(" ~ ident ~ ")" ^^ {case _ ~ _ ~ f ~ _ => CountAggregation(f)}
  | "CountDistinct" ~ "(" ~ ident ~ ")" ^^ {case _ ~ _ ~ f ~ _ => CountDistinctAggregation(f)}
  )
  
  val number: Parser[String] = """[1-9][0-9]*|0""".r
  
  /**
   * A string literal may directly contain printable characters except ' or \:
   * - to get a \, use \\
   * - to get a ', use \'
   * - to get backspace, use \b
   * - to get formfeed, use \f
   * - to get newline, use \n
   * - to get carriage return, use \r
   * - to get tab, use \t
   */
  def stringLiteral: Parser[String] =
    ("'" + """([^'\p{Cntrl}\\]|\\[\\'bfnrt])*""" + "'").r

  /**
   * Remove surrounding quotes, and replace escaped characters in a string literal.
   * Does no error checking.
   * TODO support octal and hex character codes
   *
   * @param s
   */
  def unquote(s: String): String = {
    val buf = new StringBuilder
    var i = 1
    while (i < s.length - 1) {
      s.charAt(i) match {
        case '\\' => s.charAt(i + 1) match {
          case '\\' =>
            buf.append('\\'); i += 1
          case '\'' =>
            buf.append('\''); i += 1
          case 'b' =>
            buf.append('\b'); i += 1
          case 'f' =>
            buf.append('\f'); i += 1
          case 'n' =>
            buf.append('\n'); i += 1
          case 'r' =>
            buf.append('\r'); i += 1
          case 't' =>
            buf.append('\t'); i += 1
          case c => buf.append(c); i += 1
        }

        case c => buf.append(c)
      }
      i += 1
    }
    buf.toString
  }
  
  val ident: Parser[String] = """[A-Za-z][A-Za-z0-9]*""".r
  
  override val whiteSpace = """(\s|--.*|/\*(\*(?!/)|[^*])*\*/)+""".r

  def apply(in: String) = parseAll(cmds, in)
  
  def parse(in: Reader): List[Command] = parseAll(cmds, in) match {
    case Success(result, _) => result
    case ns: NoSuccess => throw new InterpreterException(ns.msg)
  }
}
