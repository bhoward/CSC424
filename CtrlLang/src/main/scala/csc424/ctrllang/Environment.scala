package csc424.ctrllang

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
