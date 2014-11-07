object StreamSieve {
  /**
   * Prime sieve.  Filters out multiples of the first number (which
   * should be prime) from the rest of the stream.
   */
  def sieve(s: Stream[Int]): Stream[Int] =
    s.head #:: sieve(s.tail filter { _ % s.head != 0 })
    
  /**
   * Infinite stream of prime numbers.
   */
  val primes = sieve(Stream.from(2))
  
  def main(args: Array[String]): Unit = {
    import TimedTest.time
    
    val (result, t) = time { primes.take(1000).toList }
    
    println("First 10 primes: " + result.take(10))
    println("  time taken: " + t)
  }
}
