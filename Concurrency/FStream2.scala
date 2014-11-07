import scala.concurrent._
import ExecutionContext.Implicits.global

trait FStream2[+T] {
  def head: Future[T]
  def tail: Future[FStream2[T]]

  def map[U](f: T => U): FStream2[U] = {
    FStream2(for (h <- head; t <- tail)
      yield FStream2.cons(f(h), t map f))
  }

  def filter(p: T => Boolean): FStream2[T] = {
    FStream2(for (h <- head; t <- tail)
      yield if (p(h)) FStream2.cons(h, t filter p) else t filter p)
  }

  def take(n: Int): Future[List[T]] = {
    if (n == 0) Future { Nil }
    else for (h <- head; t <- tail; t2 <- t.take(n - 1))
      yield h :: t2
  }
}

object FStream2 {
  def apply[T](x: Future[FStream2[T]]): FStream2[T] = new FutureFStream2(x)
  def cons[T](h: => T, t: => FStream2[T]): FStream2[T] = new ConsFStream2(h, t)
  def from(n: Int): FStream2[Int] = cons(n, from(n + 1))
}

class ConsFStream2[T](h: => T, t: => FStream2[T]) extends FStream2[T] {
  def head = Future { h }
  def tail = Future { t }
}

class FutureFStream2[T](x: Future[FStream2[T]]) extends FStream2[T] {
  def head = x flatMap { _.head }
  def tail = x flatMap { _.tail }
}
