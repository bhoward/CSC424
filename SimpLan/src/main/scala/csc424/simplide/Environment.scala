package csc424.simplide

/**
 * An Environment maintains bindings between identifiers and values.
 */
trait Environment[+T] {
  /**
   * Retrieve the value associated with the given identifier.
   *
   * @param id the name of the identifier
   * @return the value bound to id; throw an error if not found
   */
  def apply(id: String): T
}

/**
 * The EmptyEnvironment has nothing bound.
 */
object EmptyEnvironment extends Environment[Nothing] {
  def apply(id: String): Nothing = sys.error("Identifier not found: " + id)
}

/**
 * A ChildEnvironment manages the bindings from one scope; if the identifier
 * is not found, it delegates the search to a parent Environment.
 */
class ChildEnvironment[T](parent: Environment[T]) extends Environment[T] {
  private var bindings = Map[String, T]()

  /**
   * Add a new binding to this Environment. Throws an error if the identifier
   * is already bound in this scope (but not if it is only bound in a parent
   * or more distant ancestor).
   *
   * @param id the name of the identifier
   * @param v the value to be bound to id
   */
  def addBinding(id: String, v: T): Unit = {
    if (bindings.contains(id)) sys.error("Duplicate identifier: " + id)
    else bindings += (id -> v)
  }

  def apply(id: String): T = {
    if (bindings.contains(id)) bindings(id)
    else parent(id)
  }
}
