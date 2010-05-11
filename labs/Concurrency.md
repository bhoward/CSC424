---
layout: default
title: Approaches to Concurrency Lab
---
# Approaches to Concurrency

## Futures
One of the simplest models is known as dataflow or declarative concurrency, where the only way to exchange information between two tasks is through single-assignment variables.  A *future* is the common case where the variable is assigned a result when its task terminates.  The idea is that a future represents a value which might not be ready yet; users of the value will block (wait) until it is available.  Suppose task A is responsible for computing some value x.  Another task, B, can take x as a future value; you can think of x as a variable that initially contains some value, such as a null pointer, which indicates that x has not been assigned yet.  Tasks A and B are then free to execute in parallel.  If task B tries to extract a value from x before it has been assigned, then B will be blocked.  When task A assigns a value to x, any blocked tasks waiting for x will be enabled to continue.  Since x is only a single-assignment variable, there will be no race conditions where another task might see different values of x at different times.

Semantically, getting a value from a future is no different from lazily evaluating an expression, except that the evaluation of the expression might have been started before it was requested.  Therefore, programming with futures can be seen as merely an optimization of common functional programming style. With multiple processors (or multiple cores), this helps the system to optimistically schedule some of the upcoming work in parallel, before it is needed, in the hope of improving the overall throughput.

Consider the following Scala functions:
<script src="http://gist.github.com/393863.js?file=Fibonacci.scala">
</script>
(Note: the point here is *not* to compute Fibonacci numbers efficiently -- the exponential-time Fibonacci algorithm is deliberately chosen to be something simple that can take a long time to compute for relatively small arguments.)

**Exercise:** Investigate the running time of these two versions of the Fibonacci function (and make sure that they compute the same answers).  These functions will only work up to `n=45`; beyond that, the result overflows an `Int` (and it will take too long, parallel or not).  Here is a framework which you might find useful:
<script src="http://gist.github.com/393878.js?file=TimedTest.scala">
</script>

On a computer with at least two processors or cores, you should see a significant difference in the time taken for `parfib`.  The `future` function spawns a new task to compute its argument, and returns the result as a future, `f2`.  Later, when that result is requested by applying the future to an empty argument (`f2()`), the hope is that it will have already been computed in parallel, or at least that it will have made significant progress.  Since the two calls to `fib` should each take a long time (when `parfib` is called with a large argument, say around 40), being able to compute both at the same time is a big win.

**Exercise:** Explore what happens when you modify the above definition of `parfib` so that it calls itself recursively, instead of `fib`.  What is going wrong?  Now modify it further so that `parfib(n)` hands off calculation to `fib(n)` whenever `n` is below some cutoff value (try various cutoffs between 30 and 40).  Explain what is going on, and think of a situation where this version might be better than the others we have seen.

### Streams
One generalization of futures which is possible in dataflow concurrency is the stream.  Think of the typical implementation of a singly-linked list, where instead of a null pointer at the end of the list you find a single-assignment variable.  One task, the "producer", is responsible for computing new values to attach to the stream.  When it has a new value, it assigns to the current end-of-list variable a reference to a new list node containing that value.  The "next pointer" out of that node is another single-assignment variable, and the task continues.  Now other, "consumer", tasks may read from the stream; when they reach the current end of the list, they will block until the next value is available.  A nice companion to this behavior is some notion of laziness, so that the producer waits until a consumer has reached the last value before starting to compute the next value; this prevents the producer from getting too far ahead.

Here is a rather inefficient implementation of this idea (NOTE: when Akka implements dataflow, replace this with using their dataflow variables and streams):
<script src="http://gist.github.com/393892.js?file=FStream.scala">
</script>
The idea here is that a `LazyFStream` will only start evaluating the rest of the stream when the tail is requested.  At that point, it returns a `FutureFStream` containing a future that refers to the computation in progress.  A `FutureFStream` is merely a proxy which delegates requests for the head or tail to the future, blocking until they are available.  For this toy example, our stream only implements the `filter` and `take` methods, which are needed below; a complete version would support all of the usual collection operations.

Given this datatype, we may write a prime sieve as follows (although this will serve our purposes here, this is not technically the Sieve of Eratosthenes; see [this paper](http://www.cs.hmc.edu/~oneill/papers/Sieve-JFP.pdf) where it is referred to as the "unfaithful" sieve):
<script src="http://gist.github.com/393897.js?file=Sieve.scala">
</script>
Although `primes` represents an infinite stream of prime numbers, no real work has been done yet.  To start the computation going, we need to request values from the stream:
~~~
> primes take 10
List(2, 3, 5, 7, 11, 13, 17, 19, 23, 29)
~~~

**Exercise:** As mentioned, this implementation of streams is not very efficient.  Perform timing comparisons with a prime sieve built using the `Stream[Int]` class from the Scala library.  The functions `from` and `sieve` will need to be changed by replacing `FStream` with `Stream` everywhere; furthermore, the `take` method on `Stream[Int]` returns another stream, so you will have to force it into a list to actually perform the computation: *e.g.*. `primes take 10 toList`.

**Exercise:** The real problem with the sieve example is that it doesn't do enough work per future.  Come up with an example (it may be very artificial -- for example, you could simply insert a delay loop such as `for (_ <- 1 to 1000000) {}` at an appropriate place in the sieve code) where the `FStream` implementation is faster than the Scala library `Stream` when run on multiple processors.

## Actors
While dataflow concurrency is a useful abstraction, it is limited in what it can express.  In particular, there can be no non-deterministic behavior -- that is, every execution must produce the same result by following the same evaluation course.  The only difference between executing a program with futures or dataflow streams and executing the equivalent purely functional program is that some of the computation may be sped up by proceeding in parallel.  It is possible to extend the dataflow model slightly to move beyond this restriction, by allowing a consumer of a future (or other dataflow variable) to query whether a request for its value would block; this could allow the consumer to choose to do something else while waiting.  Since this exposes the timing details, it allows them to affect the progress of the computation.  This destroys the guarantees of the dataflow model, but opens up the much more expressive model of actors exchanging messages.

The Actor model was introduced by Carl Hewitt in 1973, and has inspired a great amount of research and influenced the design of a large number of languages, including Scheme and Erlang.  As with the dataflow model, the tasks do not share any state (thus there can be no contention over writing values into shared memory); instead, the only way to exchange information between tasks is by sending a message.  This has a similar real-world inspiration as object-oriented programming (indeed, Hewitt was inspired in part by Simula 67, the first o-o programming language), except that instead of treating a message-send as a procedure call that will return a value, the messages in the Actor model are sent *asynchronously*.  That is, the sender does not need to wait for the recipient to receive the message before continuing; each actor proceeds concurrently, and only handles the messages in its "mailbox" when it is ready.

In Scala, actors are implemented as classes in the library, in the package `scala.actors` (the `Future` class is defined here as well, because Scala implements futures on top of the more general actors).  There are several ways to create and use actors in Scala; read the documentation if you want to explore some of the design considerations we will be skipping over here.

Consider this simple actor which serves as a bank account:
<script src="http://gist.github.com/396811.js?file=Account.scala">
</script>
You can set an `Account` instance in motion by calling the `start` method on a new `Account` object:
{% highlight scala %}
val savings = new Account
savings.start
{% endhighlight %}
Now you interact with the account actor by sending it messages:
{% highlight scala %}
savings ! Deposit(100)
{% endhighlight %}
This sends the object `Deposit(100)` to `savings` asynchronously.  The message send operator, `!`, can send any value as a message; if an actor does not have a case handler for that value, it will simply remain in the mailbox.

Messages may also be sent *synchronously* with the `!?` operator.  This is essentially like performing a method call -- the sender will wait until the recipient processes the message and sends back a value in reply.  In our account actor, this is how the balance is meant to be retrieved:
{% highlight scala %}
println("Current balance is " + (savings !? Balance))
{% endhighlight %}
There is a third message send operator which is of interest here: `savings !! Balance` will return immediately with a future value representing the eventual response to the message.  This allows the sender to continue processing while the recipient works on the request, and only block if the future's value is requested before the reply has been sent.

**Exercise:** Extend the `Account` actor with a withdrawal message that takes the amount to withdraw as an argument.  Test that your extension works as expected, and verify that messages are processed in the order in which they were received.

Here is an extended example using actors to solve one of the classic synchronization problems, the [http://en.wikipedia.org/wiki/Sleeping_barber_problem Sleeping Barber].  The idea is to model a barbershop with one barber and a waiting room with a limited number of chairs.  If there are no customers, the barber takes a nap.  When the barber is working on a customer, new arrivals wait in the waiting room.  If all of the chairs in the waiting room are full, the arriving customer leaves.  The problem is to model all of the interactions correctly, especially in corner cases such as two customers arriving at once, or a customer arriving just as the barber goes to sleep.  The Scala code here is adapted from a [http://code.google.com/p/gparallelizer/wiki/ActorsExamples solution in Groovy].

First, here are the imports and the definitions of the message objects:
<script src="http://gist.github.com/396819.js?file=Messages.scala">
</script>

Customer actors know their name, and respond to various messages by printing their state to the console:
<script src="http://gist.github.com/396821.js?file=Customer.scala">
</script>

The barber responds to the `Enter` and `Wait` messages from the waiting room, and knows how to cut hair:
<script src="http://gist.github.com/396822.js?file=Barber.scala">
</script>

The waiting room keeps track of the customers waiting in chairs and whether the barber is sleeping. It reacts to a customer entering (sent from an external source) and the barber calling "next!":
<script src="http://gist.github.com/396824.js?file=WaitingRoom.scala">
</script>

Finally, here is a simple test program which randomly sends 10 customers into the barbershop:
<script src="http://gist.github.com/396827.js?file=BarberTest.scala">
</script>

**Exercise:** Adjust the customer arrival and service times, and waiting room capacity, to try to explore all the possible behaviors of this simulation.  As a more challenging exercise, try to change the simulation to have two or more barbers in the same shop.

## Transactional Memory
Occasionally you might find it necessary to share memory between tasks in order to achieve a greater level of efficiency.  Consider the bank account example from the actor section above.  Suppose that, instead of one account, the program were managing millions of accounts, responding to deposit, withdrawal, and balance requests from thousands of locations.  Clearly, it would be too inefficient to have a single actor handling all of these requests -- that design doesn't "scale".  It might work to have one actor per account, but even in systems (such as Erlang and Scala) which can efficiently track many thousands of actors, this might be a needless waste of resources, since most accounts will see at most a few transactions per day.

There is a more serious potential problem with using separate actors for each account, at least if it is not implemented carefully.  Internally, banks don't really operate in terms of individual deposits and withdrawals; instead, every operation is a *transfer* of money between two accounts.  It is crucial that the total amount of money in the system is conserved (except at the boundaries where actual cash is exchanged, or funds are transferred with another institution), so a deposit to one account has to accompany a withdrawal from another.

Imagine actor code to do this:
{% highlight scala %}
...
react {
  // This is the public message:
  case Transfer(amount, to) => synchronized {
    balance -= amount
    to !? Deposit(amount)
  }

  // Somehow guarantee that this message only comes from other accounts:
  case Deposit(amount) => balance += amount; reply()
}
...
{% endhighlight %}
The `synchronized` method and the synchronous message send (!?) are essential to make sure that the withdrawal and the deposit both happen without being interrupted -- what if an audit came along between the two steps?  Unfortunately, this can lead to deadlock: suppose we get simultaneous requests to transfer one amount from Alice to Bob and another amount from Bob to Alice.  Now Alice and Bob will each enter their synchronized blocks (meaning they can't be interrupted until they finish), each will subtract an amount from their own balance, and then each will send a synchronous message to the other to deposit that amount.  Since neither can be interrupted, however, neither one of them will be able to reply to the deposit message -- deadlock.

There are solutions to this; for example, we could order the accounts by account number, and only perform transfers (possibly of negative amounts) from lower to higher numbered accounts.  This guarantees that there can't be cycles such as we saw between Alice and Bob.  However, this is very hard to get right as the system grows more complex.

One proposed alternative is the concept of "transactional memory", which is modeled on the principles of database transactions.  Database systems are able to maintain integrity constraints, such as "the total amount of money remains constant", by wrapping each data access in a transaction.  For a transaction to succeed, all of its steps have to succeed.  There are many ways to implement this model, but the effect is that the transactions occur as if each happened in isolation, one after another.  The power of a good database system is its ability to present this model while also allowing the system to scale to large amounts of throughput.

Transactional memory was originally proposed as a mechanism in hardware, another facet of the sophisticated memory mapping and caching operations found in modern computer designs.  More recently, the notion of "software transactional memory" (STM) has gained popularity, as an operating system or language-level facility to support safe access to shared memory.

One common approach to STM is to delimit blocks of operations which should be performed atomically, similar to the `synchronized` blocks in Java and Scala.  However, rather than implementing the atomicity by acquiring a lock at the start of the block, a transaction block proceeds *optimistically* -- it goes ahead and performs its operations as if it were working in isolation.  At the end of the transaction, a check is made to see if there was any interference -- for example, if one of the variables accessed by the block had its value changed by another task.  If not, then the transaction "commits" and any changes to the accessed data are made permanent.  On the other hand, if interference was detected, then the transaction fails and any changes it made are "rolled back"; at this point, the transaction starts over again.

**Exercise:** Download the akka library from [http://akkasource.org/], play with the STM examples, then implement and test the bank account simulation.

## Further Reading
 * Jonas Bonér, [http://www.slideshare.net/jboner/state-youre-doing-it-wrong-javaone-2009 State: You're Doing It Wrong - Alternative Concurrency Paradigms For The JVM] -- this is a presentation by the creator of the akka library for Scala, which provides implementations of most of the techniques discussed above.
 * Tim Bray, [http://www.tbray.org/ongoing/When/200x/2009/09/30/C-dot-next-laundry-list Concur.next - The Laundry List] -- this is one of a series of blog posts by a respected developer (currently with Google) summarizing his thoughts about what might become the "Java of Concurrency".  Here is an interesting quote from elsewhere in the series:

  I'm taking the following as an axiom: Exposing real pre-emptive threading with shared mutable data structures to application programmers is wrong. Once you get past Doug Lea, Brian Goetz, and a few people who write operating systems and database kernels for a living, it gets very hard to find humans who can actually reason about threads well enough to be usefully productive.
