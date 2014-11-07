import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

object FStreamDemo {
  /**
   * Calculate n^1000000 mod 999983 (unoptimized)
   */
  def fun(n: Int): Int = {
    var result = 1L;
    for (i <- 1 to 1000000) {
      result = result * n % 999983
    }
    result.toInt
  }

  /**
   * Check for primality by trial division (unoptimized)
   */
  def prime(n: Int): Boolean = {
    if (n < 2) return false
    for (i <- 2 until n) {
      if (n % i == 0) return false
    }
    true
  }

  def main(args: Array[String]): Unit = {
    import TimedTest.time

    val (result, t) = time {
      FStream.from(1).map(fun).filter(prime).take(100)
    }

    println(result)
    println("  FStream time taken: " + t)

    val (result2, t2) = time {
      Await.result(FStream2.from(1).map(fun).filter(prime).take(100), Duration.Inf)
    }

    println(result2)
    println("  FStream2 time taken: " + t2)

    val (results, ts) = time {
      Stream.from(1).map(fun).filter(prime).take(100).toList
    }

    println(results)
    println("  Stream time taken: " + ts)
  }
}