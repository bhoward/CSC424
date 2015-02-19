package csc480.relalg

import csc424.simplide._
import Language._

trait Command {
  def eval(env: ChildEnvironment[Table], context: ExecutionContext): Unit
}

case class QueryCommand(te: TExpr) extends Command {
  def eval(env: ChildEnvironment[Table], context: ExecutionContext): Unit = {
    context.performStep("Query")
    val t = te.eval(env)
    context.output.println(t)
    context.output.println("------")
  }
}

case class NamedQueryCommand(id: String, te: TExpr) extends Command {
  def eval(env: ChildEnvironment[Table], context: ExecutionContext): Unit = {
    context.performStep("Named Query: " + id)
    env.addBinding(id, te.eval(env))
    context.output.println(id + ": " + env(id))
    context.output.println("------")
  }
}

case class CreateCommand(id: String, schema: List[(String, Type)], data: List[List[Value]]) extends Command {
  def eval(env: ChildEnvironment[Table], context: ExecutionContext): Unit = {
    context.performStep("Create: " + id)
    val s = Schema(schema: _*)
    val t = Table(s).insert(data map {Row(_: _*)}: _*)
    env.addBinding(id, t)
    context.output.println(id + ": " + t)
    context.output.println("------")
  }
}

trait TExpr {
  def eval(env: EnvType): Table
}

case class IdTExpr(id: String) extends TExpr {
  def eval(env: EnvType): Table = env(id)
}

case class SelectTExpr(te: TExpr, cond: Condition) extends TExpr {
  def eval(env: EnvType): Table = te.eval(env).select(cond)
}

case class ProjectTExpr(te: TExpr, fields: List[String]) extends TExpr {
  def eval(env: EnvType): Table = te.eval(env).project(fields)
}

case class SortTExpr(te: TExpr, fields: List[String]) extends TExpr {
  def eval(env: EnvType): Table = te.eval(env).sort(fields)
}

case class RenameTExpr(te: TExpr, oldName: String, newName: String) extends TExpr {
  def eval(env: EnvType): Table = te.eval(env).rename(oldName, newName)
}

case class ExtendTExpr(te: TExpr, expr: Expression, field: String) extends TExpr {
  def eval(env: EnvType): Table = te.eval(env).extend(field, expr)
}

case class GroupByTExpr(te: TExpr, fields: List[String], aggs: List[Aggregation]) extends TExpr {
  def eval(env: EnvType): Table = te.eval(env).groupBy(fields, aggs map {agg => (agg.resultName, agg)})
}

case class ProductTExpr(te1: TExpr, te2: TExpr) extends TExpr {
  def eval(env: EnvType): Table = te1.eval(env).product(te2.eval(env))
}

case class JoinTExpr(te1: TExpr, te2: TExpr, cond: Condition) extends TExpr {
  def eval(env: EnvType): Table = te1.eval(env).join(te2.eval(env), cond)
}

case class SemiJoinTExpr(te1: TExpr, te2: TExpr, cond: Condition) extends TExpr {
  def eval(env: EnvType): Table = te1.eval(env).semijoin(te2.eval(env), cond)
}

case class AntiJoinTExpr(te1: TExpr, te2: TExpr, cond: Condition) extends TExpr {
  def eval(env: EnvType): Table = te1.eval(env).antijoin(te2.eval(env), cond)
}

case class OuterJoinTExpr(te1: TExpr, te2: TExpr, cond: Condition) extends TExpr {
  def eval(env: EnvType): Table = te1.eval(env).outerJoin(te2.eval(env), cond)
}

case class UnionTExpr(te1: TExpr, te2: TExpr) extends TExpr {
  def eval(env: EnvType): Table = te1.eval(env).union(te2.eval(env))
}

