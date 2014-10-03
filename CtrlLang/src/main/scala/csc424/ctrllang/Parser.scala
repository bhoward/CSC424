package csc424.ctrllang

import scala.util.parsing.combinator._
import java.io.Reader
import csc424.simplide.InterpreterException

/**
 * Combinator parser for the Control Language.
 */
object Parser extends RegexParsers with PackratParsers {
  type P[T] = PackratParser[T]
  
  lazy val expr: P[Expr] =
  ( "do" ~ rep1(stmt) ~ "in" ~ expr  ^^ {case _ ~ ss ~ _ ~ e => DoExpr(ss, e)}
  | "let" ~ rep1(decl) ~ "in" ~ expr ^^ {case _ ~ ds ~ _ ~ e => LetExpr(ds, e)}
  | expr ~ addop ~ term              ^^ {case e ~ op ~ t => BinOpExpr(op, e, t)}
  | term
  )

  lazy val term: P[Expr] =
  ( term ~ mulop ~ factor ^^ {case t ~ op ~ f => BinOpExpr(op, t, f)}
  | factor
  )

  lazy val factor: P[Expr] =
  ( "-" ~ factor     ^^ {case op ~ f => UnOpExpr(op, f)}
  | "(" ~ expr ~ ")" ^^ {case _ ~ e ~ _ => e}
  | unaryFun ~ "(" ~ expr ~ ")" ^^
      {case fun ~ _ ~ arg ~ _ => UnOpExpr(fun, arg)}
  | binaryFun ~ "(" ~ expr ~ "," ~ expr ~ ")" ^^
      {case fun ~ _ ~ arg1 ~ _ ~ arg2 ~ _ => BinOpExpr(fun, arg1, arg2)}
  | ident            ^^ {case id => IdExpr(id)}
  | number           ^^ {case numLit => NumExpr(numLit.toDouble)}
  )
  
  lazy val bexpr: P[BoolExpr] =
  ( bexpr ~ "||" ~ bterm ^^ {case e1 ~ _ ~ e2 => OrBExpr(e1, e2)}
  | bterm
  )
  
  lazy val bterm: P[BoolExpr] =
  ( bterm ~ "&&" ~ bfactor ^^ {case e1 ~ _ ~ e2 => AndBExpr(e1, e2)}
  | bfactor
  )
  
  lazy val bfactor: P[BoolExpr] =
  ( "!" ~ bfactor       ^^ {case _ ~ f => NotBExpr(f)}
  | "(" ~ bexpr ~ ")"   ^^ {case _ ~ e ~ _ => e}
  | expr ~ relop ~ expr ^^ {case e1 ~ op ~ e2 => RelBExpr(op, e1, e2)}
  | "true"              ^^ {case _ => ConstBExpr(true)}
  | "false"             ^^ {case _ => ConstBExpr(false)}
  )
  
  lazy val stmt: P[Stmt] =
  ( ident ~ "=" ~ expr ^^ {case id ~ _ ~ e => AssignStmt(id, e)}
  | "read" ~ ident     ^^ {case _ ~ id => ReadStmt(id)}
  | "write" ~ expr     ^^ {case _ ~ e => WriteStmt(e)}
  | "read" ~ stringLiteral ~ "," ~ ident ^^
      {case _ ~ prompt ~ _ ~ id => PromptReadStmt(unquote(prompt), id)}
  | "write" ~ stringLiteral ^^
      {case _ ~ msg => StringWriteStmt(unquote(msg))}
  | "swap" ~ ident ~ "," ~ ident ^^
      {case _ ~ id1 ~ _ ~ id2 => SwapStmt(id1, id2)}
  | "swapif" ~ ident ~ "," ~ ident ^^
      {case _ ~ id1 ~ _ ~ id2 => SwapIfStmt(id1, id2)}
  | "if" ~ bexpr ~ "then" ~ rep1(stmt) ~ "else" ~ rep1(stmt) ~ "endif" ^^
      {case _ ~ e ~ _ ~ s1 ~ _ ~ s2 ~ _ => IfThenElseStmt(e, s1, s2)}
  | "if" ~ bexpr ~ "then" ~ rep1(stmt) ~ "endif" ^^
      {case _ ~ e ~ _ ~ s1 ~ _ => IfThenStmt(e, s1)}
  | "while" ~ bexpr ~ "begin" ~ rep1(stmt) ~ "end" ^^
      {case _ ~ e ~ _ ~ body ~ _ => WhileStmt(e, body)}
  )
  
  lazy val decl: P[Decl] =
  ( "val" ~ ident ~ "=" ~ expr ^^ {case _ ~ id ~ _ ~ e => ValDecl(id, e)}
  | "var" ~ ident ~ "=" ~ expr ^^ {case _ ~ id ~ _ ~ e => VarDecl(id, e)}
  )

  val addop: Parser[String] = "+" | "-"

  val mulop: Parser[String] = "*" | "/" | "%"
  
  val relop: Parser[String] = "==" | "!=" | "<" | ">" | "<=" | ">="
  
  val unaryFun: Parser[String] = "sqrt" | "log" | "abs"
  
  val binaryFun: Parser[String] = "min" | "max"
  
  val ident: Parser[String] = """[A-Za-z][A-Za-z0-9]*""".r
     
  val number: Parser[String] = """([1-9][0-9]*|0)(\.[0-9]+)?([Ee][+-]?([1-9][0-9]*|0))?""".r
  
  /**
   * A string literal may directly contain printable characters except " or \:
   * - to get a \, use \\
   * - to get a ", use \"
   * - to get backspace, use \b
   * - to get formfeed, use \f
   * - to get newline, use \n
   * - to get carriage return, use \r
   * - to get tab, use \t
   */
  def stringLiteral: Parser[String] =
    ("\"" + """([^"\p{Cntrl}\\]|\\[\\"bfnrt])*""" + "\"").r

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
          case '"' =>
            buf.append('"'); i += 1
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
  
  override val whiteSpace = """(\s|//.*|/\*(\*(?!/)|[^*])*\*/)+""".r

  def apply(in: String) = parseAll(expr, in)
  
  def parse(in: Reader): Expr = parseAll(expr, in) match {
    case Success(result, _) => result
    case ns: NoSuccess => throw new InterpreterException(ns.msg)
  }
}
