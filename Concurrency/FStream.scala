import scala.actors.Future
import scala.actors.Futures._

/**
 * A stream which lazily creates future objects to compute the rest of the stream.
 * Only implements head, tail, filter, and take methods, because those are enough
 * for the toy sieve example.
 *
 * @author bhoward
 */
trait FStream[+T] {
  /**
   * @return the element at the front of the stream
   */
  def head: T

  /**
   * Requesting the rest of the stream should trigger its computation, if not already done.
   *
   * @return a stream containing all but the front element
   */
  def tail: FStream[T]

  /**
   * Create a new stream of only those elements satisfying a given test.
   *
   * @param test a Boolean-value function on stream elements
   * @return     the stream of elements x such that test(x) is true
   */
  def filter(test: T => Boolean): FStream[T] =
    if (test(head)) new LazyFStream(head, tail.filter(test))
    else new FutureFStream(future { tail.filter(test) })

  /**
   * Extract an initial list of n elements from this stream.  Forces their
   * computation if not already done.
   *
   * @param n how many elements to take from the front
   * @return  the list of n extracted elements
   */
  def take(n: Int): List[T] =
    if (n == 0) Nil
    else head :: tail.take(n - 1)
}

/**
 * Companion object with factory method to add an element at the front of a stream.
 */
object FStream {
  def cons[T](head: T, tail: => FStream[T]) = new LazyFStream(head, tail)
}

/**
 * Wraps up a head element and a block that will compute the tail, lazily.
 */
class LazyFStream[T](val head: T, rest: => FStream[T]) extends FStream[T] {
  lazy val tail = new FutureFStream(future { rest })
}

/**
 * Proxy around the future responsible for computing the stream.  Accesses will
 * block until the future has arrived.
 */
class FutureFStream[T](futch: Future[FStream[T]]) extends FStream[T] {
  def head = futch().head
  def tail = futch().tail
}