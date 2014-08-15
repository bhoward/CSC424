object ValuesAndExpressions {
  0                                               //> res0: Int(0) = 0
  42                                              //> res1: Int(42) = 42
  0x2A                                            //> res2: Int(42) = 42
  
  42.0                                            //> res3: Double(42.0) = 42.0
  4.2e1                                           //> res4: Double(42.0) = 42.0
  .42e+2                                          //> res5: Double(42.0) = 42.0
  420e-1                                          //> res6: Double(42.0) = 42.0
  
  true                                            //> res7: Boolean(true) = true
  false                                           //> res8: Boolean(false) = false
  
  '*'                                             //> res9: Char('*') = *
  '\u002A'                                        //> res10: Char('*') = *
  '\n'                                            //> res11: Char('\n') = 
                                                  //| 
  '\''                                            //> res12: Char('\'') = '

  "Hello World"                                   //> res13: String("Hello World") = Hello World
  "I said, \"Hello!\""                            //> res14: String("I said, \"Hello!\"") = I said, "Hello!"
  "line 1\nline 2"                                //> res15: String("line 1\nline 2") = line 1
                                                  //| line 2
                                                  
  """This is a string with several lines.
This is the second line, which contains a quotation: "Hello World".
This is the third line."""                        //> res16: String("This is a string with several lines.\nThis is the second line
                                                  //| , which contains a quotation: \"Hello World\".\nThis is the third line.") = 
                                                  //| This is a string with several lines.
                                                  //| This is the second line, which contains a quotation: "Hello World".
                                                  //| This is the third line.

  val x = 6                                       //> x  : Int = 6
  s"The value of x is $x, and x*7 is ${x*7}."     //> res17: String = The value of x is 6, and x*7 is 42.

  1 :: 2 :: 3 :: Nil                              //> res18: List[Int] = List(1, 2, 3)
  List(1, 2, 3)                                   //> res19: List[Int] = List(1, 2, 3)
  List(1, 2, 3).head                              //> res20: Int = 1
  List(1, 2, 3).tail                              //> res21: List[Int] = List(2, 3)

  (42, "Hello World")                             //> res22: (Int, String) = (42,Hello World)
  (42, "Hello World")._1                          //> res23: Int = 42
  (42, "Hello World")._2                          //> res24: String = Hello World

  Array(1, 2, 3)                                  //> res25: Array[Int] = Array(1, 2, 3)
  Array(1, 2, 3).size                             //> res26: Int = 3
  Array(1, 2, 3)(0)                               //> res27: Int = 1
  
  Set(1, 2, 3)                                    //> res28: scala.collection.immutable.Set[Int] = Set(1, 2, 3)
  Set(3, 1, 1, 3, 2)                              //> res29: scala.collection.immutable.Set[Int] = Set(3, 1, 2)
  Set(1, 2, 3).contains(0)                        //> res30: Boolean = false
  Set(1, 2, 3).contains(2)                        //> res31: Boolean = true

  val age = Map("Alice" -> 9, "Susanna" -> 14, "George" -> 17)
                                                  //> age  : scala.collection.immutable.Map[String,Int] = Map(Alice -> 9, Susanna 
                                                  //| -> 14, George -> 17)
  age("George")                                   //> res32: Int = 17
  age.keySet                                      //> res33: scala.collection.immutable.Set[String] = Set(Alice, Susanna, George)

  age.get("Fred")                                 //> res34: Option[Int] = None
  Some(42).get                                    //> res35: Int = 42
  // None.get
  Some(42).getOrElse(0)                           //> res36: Int = 42
  None.getOrElse(0)                               //> res37: Int = 0

  42.toString.length                              //> res38: Int = 2
}