package csc480.relalg

trait Aggregation {
  def apply(schema: Schema)(group: Iterable[Row]): Value
  
  def typeGiven(schema: Schema): Type
  
  def resultName: String
}

case class MinAggregation(name: String) extends Aggregation {
  def apply(schema: Schema)(group: Iterable[Row]): Value =
    group.map(schema(name)(_)).filter(!_.isNull).min
    
  def typeGiven(schema: Schema): Type = schema.typeOf(name)
  
  def resultName: String = "minof" + name
}

case class MaxAggregation(name: String) extends Aggregation {
  def apply(schema: Schema)(group: Iterable[Row]): Value =
    group.map(schema(name)(_)).filter(!_.isNull).max
    
  def typeGiven(schema: Schema): Type = schema.typeOf(name)
  
  def resultName: String = "maxof" + name
}

case class SumAggregation(name: String) extends Aggregation {
  def apply(schema: Schema)(group: Iterable[Row]): Value =
    group.map(schema(name)(_).asInt).sum
    
  def typeGiven(schema: Schema): Type = IntType
  
  def resultName: String = "sumof" + name
}

case class SumDistinctAggregation(name: String) extends Aggregation {
  def apply(schema: Schema)(group: Iterable[Row]): Value =
    group.map(schema(name)(_).asInt).toSet.sum
    
  def typeGiven(schema: Schema): Type = IntType
  
  def resultName: String = "sumof" + name
}

case class AvgAggregation(name: String) extends Aggregation {
  def apply(schema: Schema)(group: Iterable[Row]): Value =
    group.map(schema(name)(_).asInt).sum /
    group.map(schema(name)(_)).filter(!_.isNull).size
    
  def typeGiven(schema: Schema): Type = IntType
  
  def resultName: String = "avgof" + name
}

case class AvgDistinctAggregation(name: String) extends Aggregation {
  def apply(schema: Schema)(group: Iterable[Row]): Value =
    group.map(schema(name)(_).asInt).toSet.sum /
    group.map(schema(name)(_)).filter(!_.isNull).toSet.size
    
  def typeGiven(schema: Schema): Type = IntType
  
  def resultName: String = "avgof" + name
}

case class CountAggregation(name: String) extends Aggregation {
  def apply(schema: Schema)(group: Iterable[Row]): Value =
    group.map(schema(name)(_)).filter(!_.isNull).size
    
  def typeGiven(schema: Schema): Type = IntType
  
  def resultName: String = "countof" + name
}

case class CountDistinctAggregation(name: String) extends Aggregation {
  def apply(schema: Schema)(group: Iterable[Row]): Value =
    group.map(schema(name)(_)).filter(!_.isNull).toSet.size
    
  def typeGiven(schema: Schema): Type = IntType
  
  def resultName: String = "countdistinctof" + name
}
