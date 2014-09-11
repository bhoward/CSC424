package csc424

object Common {
  type ValueType = Int
}

import Common._

//-------------------------------------------------------------------

/**
 * A Cell acts as a storage place for a value. It is an abstraction
 * of a memory location.
 */
trait Cell {
  /**
   * Retrieve the value held in this Cell.
   */
  def value: ValueType

  /**
   * Change the value in this cell, if allowed.
   */
  def value_=(n: ValueType): Unit
}

/**
 * A ValCell is a read-only storage location. Attempting to modify the
 * value will throw an error.
 */
case class ValCell(value: ValueType) extends Cell {
  def value_=(n: ValueType) = sys.error("Attempt to change read-only value")
}

/**
 * A VarCell is a modifiable storage location. The value may be changed.
 */
case class VarCell(var value: ValueType) extends Cell

//-------------------------------------------------------------------

/**
 * An Environment maintains bindings between identifiers and storage cells.
 */
trait Environment {
  /**
   * Retrieve the Cell associated with the given identifier.
   *
   * @param id the name of the identifier
   * @return the Cell bound to id; throw an error if not found
   */
  def apply(id: String): Cell
}

/**
 * The EmptyEnvironment has nothing bound.
 */
object EmptyEnvironment extends Environment {
  def apply(id: String) = sys.error("Identifier not found: " + id)
}

/**
 * A ChildEnvironment manages the bindings from one scope; if the identifier
 * is not found, it delegates the search to a parent Environment.
 */
class ChildEnvironment(parent: Environment) extends Environment {
  private var bindings = Map[String, Cell]()

  /**
   * Add a new binding to this Environment. Throws an error if the identifier
   * is already bound in this scope (but not if it is only bound in a parent
   * or more distant ancestor).
   *
   * @param id the name of the identifier
   * @param c the Cell to be bound to id
   */
  def addBinding(id: String, c: Cell): Unit = {
    if (bindings.contains(id)) sys.error("Duplicate identifier: " + id)
    else bindings += (id -> c)
  }

  def apply(id: String): Cell = {
    if (bindings.contains(id)) bindings(id)
    else parent(id)
  }
}

//-------------------------------------------------------------------

/**
 * The ExprLangInterpreter defines the functions needed to interpret
 * an abstract syntax tree (Expr, Stmt, Decl) in the Expression Language.
 */
object ExprLangInterpreter {
  /**
   * Evaluate an Expr in the given Environment.
   *
   * @param expr the abstract syntax tree for the expression
   * @param env the Environment binding ids to cells
   * @return the value of the expression
   */
  def eval(expr: Expr, env: Environment): ValueType = expr match {
    case NumExpr(n) => n
    case IdExpr(id) => env(id).value
    case UnOpExpr("-", e) => -eval(e, env)
    case BinOpExpr("+", e1, e2) => eval(e1, env) + eval(e2, env)
    case BinOpExpr("-", e1, e2) => eval(e1, env) - eval(e2, env)
    case BinOpExpr("*", e1, e2) => eval(e1, env) * eval(e2, env)
    case BinOpExpr("/", e1, e2) => eval(e1, env) / eval(e2, env)
    case BinOpExpr("%", e1, e2) => eval(e1, env) % eval(e2, env)
    case LetExpr(ds, e) => eval(e, elaborate(ds, env))
    case DoExpr(ss, e) => exec(ss, env); eval(e, env)
    case _ => sys.error("Unrecognized expression: " + expr)
  }

  /**
   * Execute a list of statements in the given Environment. Does not return
   * a value, because statements are executed solely for their side-effects.
   *
   * @param ss the abstract syntax trees of the statements
   * @param env the Environment binding ids to cells
   */
  def exec(ss: List[Stmt], env: Environment): Unit = ss map {
    case ReadStmt(id) =>
      // Ask for user input. Use the variable name as the prompt
      print(id + "? ")
      env(id).value = readInt

    case WriteStmt(e) =>
      // Print the result of evaluating an expression
      println(eval(e, env))

    case AssignStmt(id, e) =>
      // Modify the value stored in the cell bound to a variable
      env(id).value = eval(e, env)
  }

  /**
   * Process a list of declarations in the given Environment, producing
   * a new (child) Environment containing the appropriate bindings.
   *
   * @param ds the abstract syntax trees of the declarations
   * @param env the (parent) Environment binding ids to cells
   * @return the new elaborated Environment
   */
  def elaborate(ds: List[Decl], env: Environment): Environment = {
    val child = new ChildEnvironment(env)
    
    for (decl <- ds) {
      decl match {
        // Evaluate each initial value in the parent Environment, and
        // bind a new storage cell containing that value in the child
        case ValDecl(id, e) => child.addBinding(id, ValCell(eval(e, env)))
        case VarDecl(id, e) => child.addBinding(id, VarCell(eval(e, env)))
      }
    }
    
    child
  }
}