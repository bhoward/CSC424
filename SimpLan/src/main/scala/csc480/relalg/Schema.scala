package csc480.relalg

trait Schema {
  def names: IndexedSeq[String]
  def types: IndexedSeq[Type]
  
  def apply(name: String)(row: Row): Value = row(names.indexOf(name))
  def typeOf(name: String): Type = types(names.indexOf(name))
  
  def rename(from: String, to: String): Schema
  def project(indices: Iterable[Int]): Schema
  def extend(name: String, typ: Type): Schema
  def unionPerm(that: Schema): Iterable[Int]
  def union(that: Schema): Schema
  def product(that: Schema): Schema
  
  def indices(cols: Iterable[String]): Iterable[Int]
}

object Schema {
  def apply(fields: (String, Type)*): Schema =
    new DefaultSchema(fields.toIndexedSeq map {case (name, _) => name}, fields.toIndexedSeq map {case (_, typ) => typ})
}

class DefaultSchema(val names: IndexedSeq[String], val types: IndexedSeq[Type]) extends Schema {
  def rename(from: String, to: String): Schema =
    new DefaultSchema(names map {name => if (name == from) to else name}, types)
  
  def project(indices: Iterable[Int]): Schema = {
    val names2 = for (i <- indices) yield names(i) // assumes indices all exist
    val types2 = for (i <- indices) yield types(i)
    new DefaultSchema(names2.toIndexedSeq, types2.toIndexedSeq)
  }
  
  def extend(name: String, typ: Type): Schema =
    new DefaultSchema(names :+ name, types :+ typ)
  
  // Produce permutation of this so fields match that; use -1 if no match
  def unionPerm(that: Schema): Iterable[Int] =
    for (i <- that.names.indices) yield {
      val name = that.names(i)
      val typ = that.types(i)
      val j = names.indexOf(name)
      if (j >= 0 && typ.matches(types(j))) j else -1
    }
  
  def union(that: Schema): Schema = {
    val types2 = for (i <- names.indices) yield types(i).union(that.types(that.names.indexOf(names(i)))) // assumes indices all exist
    new DefaultSchema(names, types2.toIndexedSeq)
  }
  
  def product(that: Schema): Schema =
    new DefaultSchema(names ++ that.names, types ++ that.types)
  
  // Retrieve indices of named columns that exist in this Schema
  def indices(cols: Iterable[String]): Iterable[Int] =
    cols map {name => names.indexOf(name)} filter {_ >= 0}
}
