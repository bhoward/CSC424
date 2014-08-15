object ControlStructures {
  var n = 11                                      //> n  : Int = 11

  while (n != 1) {
    println(n)
    if (n % 2 == 0) {
      // n is even
      n = n / 2;
    } else {
      // n is odd
      n = 3 * n + 1;
    }
  }                                               //> 11
                                                  //| 34
                                                  //| 17
                                                  //| 52
                                                  //| 26
                                                  //| 13
                                                  //| 40
                                                  //| 20
                                                  //| 10
                                                  //| 5
                                                  //| 16
                                                  //| 8
                                                  //| 4
                                                  //| 2

  n = 11
  while (n != 1) {
    println(n)
    n = if (n % 2 == 0) n / 2 else 3 * n + 1
  }                                               //> 11
                                                  //| 34
                                                  //| 17
                                                  //| 52
                                                  //| 26
                                                  //| 13
                                                  //| 40
                                                  //| 20
                                                  //| 10
                                                  //| 5
                                                  //| 16
                                                  //| 8
                                                  //| 4
                                                  //| 2

  var i = 12                                      //> i  : Int = 12
  var j = 18                                      //> j  : Int = 18
  val x = {
    while (j != 0) {
      val temp = j
      j = i % j
      i = temp
    }
    i
  }                                               //> x  : Int = 6

  for (n <- List(3, 1, 4, 1, 5)) {
    println(n)                                    //> 3
                                                  //| 1
                                                  //| 4
                                                  //| 1
                                                  //| 5
  }

  for (i <- 0 until 5) println(i)                 //> 0
                                                  //| 1
                                                  //| 2
                                                  //| 3
                                                  //| 4

  for (i <- 0 to 5) println(i)                    //> 0
                                                  //| 1
                                                  //| 2
                                                  //| 3
                                                  //| 4
                                                  //| 5

  for (i <- 0 to 5 by 2) println(i)               //> 0
                                                  //| 2
                                                  //| 4

  for (i <- 5 to 0 by -2) println(i)              //> 5
                                                  //| 3
                                                  //| 1

  for (i <- List(3, 1, 4, 1, 5)) yield i * i      //> res0: List[Int] = List(9, 1, 16, 1, 25)

  "Susanna" match {
    case "Alice" => 9
    case "Susanna" => 14
    case "George" => 17
    case _ => -1
  }                                               //> res1: Int = 14

  "Fred" match {
    case "Alice" => 9
    case "Susanna" => 14
    case "George" => 17
    case _ => -1
  }                                               //> res2: Int = -1

  List(1, 2, 3) match {
    case Nil => println("The list is empty")
    case h :: t => println(s"The head is $h and the tail is $t")
  }                                               //> The head is 1 and the tail is List(2, 3)

  val front = List(1, 2, 3) match {
    case Nil => sys.error("Empty list")
    case f :: _ => f
  }                                               //> front  : Int = 1

  (42, "Hello World") match {
    case (n, s) => "First is " + n + ", second is " + s
  }                                               //> res3: String = First is 42, second is Hello World

  var a: Option[Int] = Some(42)                   //> a  : Option[Int] = Some(42)
  a match {
    case Some(t) => t
    case None => 0
  }                                               //> res4: Int = 42

  a = None
  a match {
    case Some(t) => t
    case None => 0
  }                                               //> res5: Int = 0

  var p: (List[Int], List[Int]) = (List(1, 2, 3), List(4, 5, 6))
                                                  //> p  : (List[Int], List[Int]) = (List(1, 2, 3),List(4, 5, 6))
  p match {
    case (x :: _, y :: _) => Some(x * y)
    case _ => None
  }                                               //> res6: Option[Int] = Some(4)

  p = (List(1, 2, 3), List())
  p match {
    case (x :: _, y :: _) => Some(x * y)
    case _ => None
  }                                               //> res7: Option[Int] = None

  for (x <- List(List(42, 21), List(42), List(21), List())) yield
  x match {
    case _ :: _ :: _ => "A"
    case 42 :: _ => "B"
    case _ :: _ => "C"
    case _ => "D"
  }                                               //> res8: List[String] = List(A, B, C, D)
}