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
      val fibNminus1 = future { fib(n - 1) }
      val fibNminus2 = future { fib(n - 2) }
      Await.result(
        for {
          f1 <- fibNminus1
          f2 <- fibNminus2
        } yield f1 + f2,
        Duration.Inf)
    }
}
