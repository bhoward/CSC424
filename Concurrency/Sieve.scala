object Sieve {
  /**
   * Prime sieve.  Filters out multiples of the first number (which
   * should be prime) from the rest of the stream.
   */
  def sieve(s: FStream[Int]): FStream[Int] =
    FStream.cons(s.head, sieve(s.tail filter { _ % s.head != 0 }))

  /**
   * Infinite stream of prime numbers.
   */
  val primes = sieve(FStream.from(2))
  
  def main(args: Array[String]): Unit = {
    import TimedTest.time
    
    val (result, t) = time { primes.take(1000) }
    
    println("First 10 primes: " + result.take(10))
    println("  time taken: " + t)
  }
}
