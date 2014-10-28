package csc424.ctrllang

import csc424.simplide._
import Language._

/**
 * The Interpreter defines the functions needed to interpret
 * an abstract syntax tree (Expr, BExpr, Stmt, Decl) in the Control Language.
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
  def eval(expr: Expr, env: EnvType): Result = {
    context.performStep("Eval: " + expr)
    expr match {
      case NumExpr(n) => n
      case IdExpr(id) => env(id).value
      case UnOpExpr("-", e) => -eval(e, env)
      case UnOpExpr("sqrt", e) => math.sqrt(eval(e, env))
      case UnOpExpr("log", e) => math.log(eval(e, env))
      case UnOpExpr("abs", e) => math.abs(eval(e, env))
      case BinOpExpr("+", e1, e2) => eval(e1, env) + eval(e2, env)
      case BinOpExpr("-", e1, e2) => eval(e1, env) - eval(e2, env)
      case BinOpExpr("*", e1, e2) => eval(e1, env) * eval(e2, env)
      case BinOpExpr("/", e1, e2) => eval(e1, env) / eval(e2, env)
      case BinOpExpr("%", e1, e2) => eval(e1, env) % eval(e2, env)
      case BinOpExpr("min", e1, e2) => math.min(eval(e1, env), eval(e2, env))
      case BinOpExpr("max", e1, e2) => math.max(eval(e1, env), eval(e2, env))
      case LetExpr(ds, e) => eval(e, elaborate(ds, env))
      case CondExpr(be, e1, e2) => if (beval(be, env)) eval(e1, env) else eval(e2, env)
      case DoExpr(ss, e) => exec(ss, env); eval(e, env)
      case _ => sys.error("Unrecognized expression: " + expr)
    }
  }
  
  /**
   * Evaluate a BoolExpr in the given Environment.
   * 
   * @param bexpr the abstract syntax tree for the boolean expression
   * @param env the Environment binding ids to cells
   * @return the value of the expression
   */
  def beval(bexpr: BoolExpr, env: EnvType): Boolean = {
    context.performStep("BEval: " + bexpr)
    bexpr match {
      case RelBExpr("==", left, right) => eval(left, env) == eval(right, env)
      case RelBExpr("!=", left, right) => eval(left, env) != eval(right, env)
      case RelBExpr("<", left, right) => eval(left, env) < eval(right, env)
      case RelBExpr(">", left, right) => eval(left, env) > eval(right, env)
      case RelBExpr("<=", left, right) => eval(left, env) <= eval(right, env)
      case RelBExpr(">=", left, right) => eval(left, env) >= eval(right, env)
      case ConstBExpr(const) => const
      case AndBExpr(left, right) => beval(left, env) && beval(right, env)
      case OrBExpr(left, right) => beval(left, env) || beval(right, env)
      case NotBExpr(e) => !beval(e, env)
      case _ => sys.error("Unrecognized boolean operation: " + bexpr)
    }
  }

  /**
   * Execute a list of statements in the given Environment. Does not return
   * a value, because statements are executed solely for their side-effects.
   *
   * @param ss the abstract syntax trees of the statements
   * @param env the Environment binding ids to cells
   */
  def exec(ss: List[Stmt], env: EnvType): Unit = ss map {stmt =>
    context.performStep("Exec: " + stmt)
    stmt match {
      case AssignStmt(id, e) =>
        // Modify the value stored in the cell bound to a variable
        val cell = env(id).cell
        cell.contents = eval(e, env)
        
      case ReadStmt(id) =>
        // Ask for user input. Use the variable name as the prompt
        context.output.print(id + "? ")
        val cell = env(id).cell
        cell.contents = read(context.input)
  
      case WriteStmt(e) =>
        // Print the result of evaluating an expression
        context.output.println(showResult(eval(e, env)))
  
      case PromptReadStmt(prompt, id) =>
        // Ask for user input. Use the given prompt
        context.output.print(prompt)
        val cell = env(id).cell
        cell.contents = read(context.input)
          
      case StringWriteStmt(msg) =>
        // Print a literal string
        context.output.println(msg)
          
      case SwapStmt(id1, id2) =>
        // Swap the values of two variables
        val cell1 = env(id1).cell
        val cell2 = env(id2).cell
        var temp = cell1.contents
        cell1.contents = cell2.contents
        cell2.contents = temp
          
      case SwapIfStmt(id1, id2) =>
        // Swap the values of two variables if the first is larger than the second
        if (env(id1).value > env(id2).value) {
          val cell1 = env(id1).cell
          val cell2 = env(id2).cell
          var temp = cell1.contents
          cell1.contents = cell2.contents
          cell2.contents = temp
        }
  
      case IfThenElseStmt(test, trueClause, falseClause) =>
        // Execute the trueClause or falseClause depending on a boolean test
        if (beval(test, env)) {
          exec(trueClause, env)
        } else {
          exec(falseClause, env)
        }
  
      case IfThenStmt(test, trueClause) =>
        // Execute the trueClause if the boolean test is true
        if (beval(test, env)) {
          exec(trueClause, env)
        }
  
      case WhileStmt(test, body) =>
        // Repeatedly execute the body as long as the test is true.
        // The test is checked before each execution of the body
        while (beval(test, env)) {
          exec(body, env)
        }
    }
  }

  /**
   * Process a list of declarations in the given Environment, producing
   * a new (child) Environment containing the appropriate bindings.
   *
   * @param ds the abstract syntax trees of the declarations
   * @param env the (parent) Environment binding ids to cells
   * @return the new elaborated Environment
   */
  def elaborate(ds: List[Decl], env: EnvType): EnvType = {
    val child = new ChildEnvironment(env)
    
    ds map {decl =>
      context.performStep("Elab: " + decl)
      decl match {
        // Evaluate each initial value in the parent Environment, and
        // bind a new entry containing that value in the child
        case ValDecl(id, e) => child.addBinding(id, ConstValue(eval(e, env)))
        case VarDecl(id, e) => child.addBinding(id, CellValue(new Cell(eval(e, env))))
      }
    }
    
    child
  }
}