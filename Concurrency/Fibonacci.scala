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
      val f1 = Future { fib(n - 1) }
      val f2 = Future { fib(n - 2) }
      val a = Await.result(f1, Duration.Inf)
      val b = Await.result(f2, Duration.Inf)
      a + b
    }

  def parfib2(n: Int): Int =
    if (n < 2) 1
    else {
      val f1 = Future { fib(n - 1) }
      val f2 = Future { fib(n - 2) }
      val sum = for {
        a <- f1
        b <- f2
      } yield a + b
      Await.result(sum, Duration.Inf)
    }

  def parfib3(n: Int): Int =
    if (n < 2) 1
    else {
      val f1 = Future { fib(n - 1) }
      val f2 = Future { fib(n - 2) }
      val sum = Promise[Int]
      f1 onSuccess {
        case a =>
          f2 onSuccess {
            case b =>
              sum.success(a + b)
          }
      }
      Await.result(sum.future, Duration.Inf)
    }

  def main(args: Array[String]): Unit = {
    import TimedTest._

    val (fibResult, fibTime) = time { fib(45) }
    println("Result from plain Fibonacci = " + fibResult)
    println("  time taken: " + fibTime)

    val (pfResult, pfTime) = time { parfib(45) }
    println("Result from parallel Fibonacci = " + pfResult)
    println("  time taken: " + pfTime)

    val (pf2Result, pf2Time) = time { parfib2(45) }
    println("Result from parallel Fibonacci (v2) = " + pf2Result)
    println("  time taken: " + pf2Time)

    val (pf3Result, pf3Time) = time { parfib3(45) }
    println("Result from parallel Fibonacci (v3) = " + pf3Result)
    println("  time taken: " + pf3Time)
  }
}
