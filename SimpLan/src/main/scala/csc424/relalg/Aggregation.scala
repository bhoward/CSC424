package csc424.relalg

trait Aggregation {
  def apply(schema: Schema)(group: Iterable[Row]): Value
  
  def typeGiven(schema: Schema): Type
  
  def resultName: String
}

case class MinAggregation(name: String) extends Aggregation {
  def apply(schema: Schema)(group: Iterable[Row]): Value =
    group.map(schema(name)(_)).min
    
  def typeGiven(schema: Schema): Type = schema.typeOf(name)
  
  def resultName: String = "MinOf" + name
}

case class MaxAggregation(name: String) extends Aggregation {
  def apply(schema: Schema)(group: Iterable[Row]): Value =
    group.map(schema(name)(_)).max
    
  def typeGiven(schema: Schema): Type = schema.typeOf(name)
  
  def resultName: String = "MaxOf" + name
}

case class SumAggregation(name: String) extends Aggregation {
  def apply(schema: Schema)(group: Iterable[Row]): Value =
    group.map(schema(name)(_).asInt).sum
    
  def typeGiven(schema: Schema): Type = IntType
  
  def resultName: String = "SumOf" + name
}

case class AvgAggregation(name: String) extends Aggregation {
  def apply(schema: Schema)(group: Iterable[Row]): Value =
    group.map(schema(name)(_).asInt).sum / group.size
    
  def typeGiven(schema: Schema): Type = IntType
  
  def resultName: String = "AvgOf" + name
}

case class CountAggregation(name: String) extends Aggregation {
  def apply(schema: Schema)(group: Iterable[Row]): Value =
    group.size
    
  def typeGiven(schema: Schema): Type = IntType
  
  def resultName: String = "CountOf" + name
}

case class CountDistinctAggregation(name: String) extends Aggregation {
  def apply(schema: Schema)(group: Iterable[Row]): Value =
    group.map(schema(name)(_)).toSet.size
    
  def typeGiven(schema: Schema): Type = IntType
  
  def resultName: String = "CountDistinctOf" + name
}