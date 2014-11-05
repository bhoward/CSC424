object Sieve {
  /**
   * @return the stream n, n+1, n+2, ...
   */
  def from(n: Int): FStream[Int] = FStream.cons(n, from(n + 1))

  /**
   * Prime sieve.  Filters out multiples of the first number (which
   * should be prime) from the rest of the stream.
   */
  def sieve(s: FStream[Int]): FStream[Int] =
    FStream.cons(s.head, sieve(s.tail filter { _ % s.head != 0 }))

  /**
   * Infinite stream of prime numbers.
   */
  val primes = sieve(from(2))
  
  def main(args: Array[String]): Unit = {
    println(primes take 10)
  }
}