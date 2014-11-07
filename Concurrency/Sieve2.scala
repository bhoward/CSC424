import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

object Sieve2 {
  /**
   * Prime sieve.  Filters out multiples of the first number (which
   * should be prime) from the rest of the stream.
   */
  def sieve(s: FStream2[Int]): FStream2[Int] =
    FStream2(for (h <- s.head; t <- s.tail)
      yield FStream2.cons(h, t filter { _ % h != 0 }))

  /**
   * Infinite stream of prime numbers.
   */
  val primes = sieve(FStream2.from(2))

  def main(args: Array[String]): Unit = {
    import TimedTest.time

    val (result, t) = time { Await.result(primes.take(1000), Duration.Inf) }

    println("First 10 primes: " + result.take(10))
    println("  time taken: " + t)
  }
}
