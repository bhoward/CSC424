package csc424.relalg

object Test {
  def main(args: Array[String]): Unit = {
    val s = Schema("name" -> StringType, "ssn" -> IntType)
    val t = Table(s).insert(
          Row("Harv", 987654321),
          Row("Ralph", 555555555),
          Row("Fred", 123456789),
          Row("Phred", 123456789)
        )
        
    println((t product t.rename("ssn", "ssn2").rename("name", "name2"))
        .sort(Vector("ssn", "name2"))
        .extend("foo", 1 + F("ssn2"))
        .select(F("foo") > F("ssn"))
        .project(Vector("name", "foo"))
        .rename("foo", "ssn") union t)
    println()
    
    println((t product t.rename("ssn" -> "ssn2", "name" -> "name2"))
        .sort("ssn", "name2")
        .extend("foo" -> (1 + F("ssn2")))
        .select(F("foo") > F("ssn"))
        .project("name", "foo")
        .rename("foo" -> "ssn") union t)
    println()
    
    val s2 = Schema("follower" -> IntType, "followee" -> IntType)
    val t2 = Table(s2).insert(
          Row(123456789, 555555555),
          Row(123456789, 987654321),
          Row(555555555, 987654321)
        )
        
    println(t.join(t2, F("ssn") == F("follower"))
        .join(t.rename("name" -> "handle", "ssn" -> "id"), F("id") == F("followee"))
        .project("handle", "name"))
    println()
        
    println(t.join(t2, F("ssn") == F("follower")))
    println()
    
    println(t.semijoin(t2, F("ssn") == F("follower")))
    println()
    
    println(t.antijoin(t2, F("ssn") == F("follower")))
    println()
    
    println(t.groupBy()(
          "minssn" -> MinAggregation("ssn"),
          "avgssn" -> AvgAggregation("ssn"),
          "maxname" -> MaxAggregation("name"),
          "num" -> CountAggregation("ssn"),
          "unum" -> CountDistinctAggregation("ssn")
          ))
    println()
          
    println(t.outerJoin(t2, F("ssn") == F("follower")))
    println()
  }
}