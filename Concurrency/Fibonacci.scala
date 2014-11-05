import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

object Fibonacci {
  def fib(n: Int): Int =
    if (n < 2) 1
    else fib(n - 1) + fib(n - 2)

  def parfib(n: Int): Int =
    if (n < 2) 1
    else {
      val f1 = future { fib(n - 1) }
      val f2 = future { fib(n - 2) }
      val a = Await.result(f1, Duration.Inf)
      val b = Await.result(f2, Duration.Inf)
      a + b
    }

  def parfib2(n: Int): Int =
    if (n < 2) 1
    else {
      val f1 = future { fib(n - 1) }
      val f2 = future { fib(n - 2) }
      val sum = for {
        a <- f1
        b <- f2
      } yield a + b
      Await.result(sum, Duration.Inf)
    }

  def main(args: Array[String]): Unit = {
    import TimedTest._
    
    val (fibResult, fibTime) = time { fib(45) }
    val (pfResult, pfTime) = time { parfib(45) }
    val (pf2Result, pf2Time) = time { parfib2(45) }
    
    println("Result from plain Fibonacci = " + fibResult)
    println("  time taken: " + fibTime)
    println("Result from parallel Fibonacci = " + pfResult)
    println("  time taken: " + pfTime)
    println("Result from parallel Fibonacci (v2) = " + pf2Result)
    println("  time taken: " + pf2Time)
  }
}
