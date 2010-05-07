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
