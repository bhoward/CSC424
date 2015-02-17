package csc424.relalg

trait Table {
  def schema: Schema
  def rows: Iterable[Row]
  
  def select(cond: Condition): Table
  def project(cols: Iterable[String]): Table
  def sort(cols: Iterable[String]): Table
  def rename(from: String, to: String): Table
  def extend(field: String, value: Expression): Table
  def groupBy(cols: Iterable[String], aggs: Iterable[(String, Aggregation)]): Table
  
  // Convenience versions of the above
  def project(cols: String*): Table = project(cols.toIterable)
  def sort(cols: String*): Table = sort(cols.toIterable)
  def rename(pairs: (String, String)*): Table =
    if (pairs.length == 0) this
    else rename(pairs.head._1, pairs.head._2).rename(pairs.tail: _*)
  def extend(pairs: (String, Expression)*): Table =
    if (pairs.length == 0) this
    else extend(pairs.head._1, pairs.head._2).extend(pairs.tail: _*)
  def groupBy(cols: String*)(aggs: (String, Aggregation)*): Table = groupBy(cols.toIterable, aggs.toIterable)
  
  def union(that: Table): Table
  def product(that: Table): Table
  def join(that: Table, cond: Condition): Table
  def semijoin(that: Table, cond: Condition): Table
  def antijoin(that: Table, cond: Condition): Table
  def outerJoin(that: Table, cond: Condition): Table
  
  def insert(rows2: Row*): Table
  // TODO update and delete
}

class DefaultTable(val schema: Schema, val rows: Iterable[Row]) extends Table {
  override def toString: String =
    schema.names.mkString(", ") + "\n" + rows.mkString("\n")
    
  def select(cond: Condition): Table =
    new DefaultTable(schema, rows filter {row => cond(schema)(row)})
  
  def project(cols: Iterable[String]): Table = {
    val indices = schema.indices(cols)
    new DefaultTable(schema.project(indices), rows map {row => row.project(indices)})
  }
  
  def sort(cols: Iterable[String]): Table = {
    val indices = schema.indices(cols)
    val ord = new LexicalRowOrdering(indices)
    new DefaultTable(schema, rows.toSeq.sorted(ord))
  }
  
  def rename(from: String, to: String): Table =
    new DefaultTable(schema.rename(from, to), rows)
  
  def extend(field: String, value: Expression): Table =
    new DefaultTable(schema.extend(field, value.typeGiven(schema)), rows map {row => row.extend(schema, value)})
  
  def groupBy(cols: Iterable[String], aggs: Iterable[(String, Aggregation)]): Table = {
    val indices = schema.indices(cols)
    val schema2 = (schema.project(indices) /: aggs){(sch, pair) =>
        val (name, agg) = pair
        sch.extend(name, agg.typeGiven(schema))
      }
    val groups = rows.groupBy(_.project(indices))
    val rows2 = for {
      (common, group) <- groups
    } yield (common /: aggs){(row, pair) =>
        val (_, agg) = pair
        row.extend(schema, agg(schema)(group))
      }
    new DefaultTable(schema2, rows2)
  }
  
  def union(that: Table): Table = {
    val indices = that.schema.unionPerm(schema)
    val schema2 = schema.union(that.schema)
    new DefaultTable(schema2, rows ++ (that.rows map {row => row.project(indices)}))
  }
  
  def product(that: Table): Table = {
    val schema2 = schema.product(that.schema)
    val rows2 = for {
      thisRow <- rows
      thatRow <- that.rows
    } yield thisRow.product(thatRow)
    new DefaultTable(schema2, rows2)
  }
  
  def join(that: Table, cond: Condition): Table =
    product(that).select(cond)
  
  def semijoin(that: Table, cond: Condition): Table = {
    val schema2 = schema.product(that.schema)
    val rows2 = rows filter {thisRow =>
      that.rows.exists {thatRow =>
        cond(schema2)(thisRow.product(thatRow))
      }
    }
    new DefaultTable(schema, rows2)
  }
  
  def antijoin(that: Table, cond: Condition): Table = {
    val schema2 = schema.product(that.schema)
    val rows2 = rows filter {thisRow =>
      !that.rows.exists {thatRow =>
        cond(schema2)(thisRow.product(thatRow))
      }
    }
    new DefaultTable(schema, rows2)
  }
  
  def outerJoin(that: Table, cond: Condition): Table = {
    val join = this.join(that, cond)
    val left = this.antijoin(that, cond).extend(that.schema.names.map {name => (name, NullValue)}: _*)
    val right = that.antijoin(this, cond).extend(this.schema.names.map {name => (name, NullValue)}: _*)
    join union left union right
  }
  
  def insert(rows2: Row*): Table =
    new DefaultTable(schema, rows ++ rows2)
}

object Table {
  def apply(schema: Schema): Table = new DefaultTable(schema, List())
}
