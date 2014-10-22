package csc424.exprlang

import csc424.simplide.ExecutionContext

/**
 * The Interpreter defines the functions needed to interpret
 * an abstract syntax tree (Expr, Stmt, Decl) in the Expression Language.
 * 
 * @param context the ExecutionContext to use for input/output
 */
class Interpreter(context: ExecutionContext) {
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
      context.output.print(id + "? ")
      env(id).value = read(context.input)

    case WriteStmt(e) =>
      // Print the result of evaluating an expression
      context.output.println(show(eval(e, env)))

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