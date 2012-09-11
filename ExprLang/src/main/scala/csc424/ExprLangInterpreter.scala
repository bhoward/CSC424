package csc424

trait Environment {
  def apply(id: String): Int
  def update(id: String, n: Int): Unit
}

object EmptyEnvironment extends Environment {
  def apply(id: String) = sys.error("Identifier not found: " + id)
  def update(id: String, n: Int) = sys.error("Identifier not found: " + id)
}

class ChildEnvironment(parent: Environment) extends Environment {
  private var bindings = Map[String, Cell]()
  
  def addBinding(id: String, c: Cell) {
    if (bindings.contains(id)) sys.error("Duplicate identifier: " + id)
    else bindings += (id -> c)
  }
  
  def apply(id: String): Int = {
    if (bindings.contains(id)) bindings(id).value
    else parent(id)
  }
  
  def update(id: String, n: Int) {
    if (bindings.contains(id)) bindings(id).value = n
    else parent(id) = n
  }
}

trait Cell {
  def value: Int
  def value_=(n: Int): Unit
}

case class ValCell(value: Int) extends Cell {
  def value_=(n: Int) = sys.error("Attempt to change read-only value")
}

case class VarCell(var value: Int) extends Cell

object ExprLangInterpreter {
  def eval(expr: Expr, env: Environment): Int = expr match {
    case NumExpr(n) => n
    case IdExpr(id) => env(id)
    case UnOpExpr("-", e) => -eval(e, env)
    case BinOpExpr("+", e1, e2) => eval(e1, env) + eval(e2, env)
    case BinOpExpr("-", e1, e2) => eval(e1, env) - eval(e2, env)
    case BinOpExpr("*", e1, e2) => eval(e1, env) * eval(e2, env)
    case BinOpExpr("/", e1, e2) => eval(e1, env) / eval(e2, env)
    case BinOpExpr("%", e1, e2) => eval(e1, env) % eval(e2, env)
    case LetExpr(ds, e) => eval(e, elaborate(ds, env))
    case DoExpr(ss, e) => exec(ss, env); eval(e, env)
  }
  
  def exec(ss: List[Stmt], env: Environment): Unit = ss map {
    case ReadStmt(id) =>
      print(id + "? ")
      env(id) = readInt
      
    case WriteStmt(e) =>
      println(eval(e, env))
      
    case AssignStmt(id, e) =>
      env(id) = eval(e, env)
  }
  
  def elaborate(ds: List[Decl], env: Environment): Environment = {
    val child = new ChildEnvironment(env)
    for (decl <- ds) {
      decl match {
        case ValDecl(id, e) => child.addBinding(id, ValCell(eval(e, env)))
        case VarDecl(id, e) => child.addBinding(id, VarCell(eval(e, env)))
      }
    }
    child
  }
}