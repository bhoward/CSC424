---
layout: default
title: Scala Overview Lab
---
# Scala Overview

## Comparison with Java

**Exercise:** Start the Scala interactive shell (also known as the REPL, which stands for Read-Eval-Print-Loop) and execute each of the following statements.  Before evaluating each, try to predict what the output will be.  Be sure to note any differences in what you might expect based on your knowledge of Java, and try to understand and explain those differences. Evaluate additional statements to test your explanations.

* `1 + 2 * 3`
* `res0 * res0`
* `res0 / 0`
* `2 ^ 6`
* `Math.pow(2, 6)`
* `"Hello World".length`
* `"Hello World".reverse`
* `42.toDouble`
* `val nums = new java.util.ArrayList[Int]()`
* `nums.add(42)`
* `nums`
* `println(nums)`
* `nums.contains(37)`
* `nums contains 42`
* `"ba" + "na" * 2`
* `"ba".+("na".*(2))`
* `1.+(2.*(3))`
* `1 .+(2 .*(3))`
* `val x = 37; var y = 42`
* `if (x < y / 2 || x > y - 5) println("Yes") else println("No")`
* `val z = if (x < y) x else y`
* `x = 17`
* `x == 17`
* `y = 17`
* `y == 17`
* `nums.add(z); println(nums)`
* `for (i <- 1 to 10) println(i)`
* `val names = List("Alice", "George", "Susanna")`
* `for (n <- names) println(n)`
* `names.foreach { n => println(n) }`
* `for (i <- nums) println(i)`
* `for (n <- names) yield n.reverse`
* `names.map { n => n.reverse }`

We will come back to a number of the above examples later in the course.

## Objects in Scala

As in Java, the object is the basic unit for organizing data and code in Scala.  In addition, Scala regularizes and generalizes the treatment of objects in several directions.  One regularization seen above is that Scala hides the distinction between primitive and object types: `42` is an object belonging to class `Int`, which provides various methods such as `toDouble`.

Consider the following Java code, defining a simple hierarchy of shape classes:
{% highlight java %}
public interface Shape {
  Point getLocation();
  void setLocation(Point center);
  void draw(Graphics g);
}

public class Square implements Shape {
  private Point center;
  private int width;
  
  public Square(Point center, int width) {
    this.center = center;
    this.width = width;
  }
  
  public Point getLocation() {
    return center;
  }
  
  public void setLocation(Point center) {
    this.center = center;
  }
  
  public int getWidth() {
    return width;
  }
  
  public void draw(Graphics g) {
    g.drawRect(center.x - width/2, center.y - width/2, width, width);
  }
}

public class Circle implements Shape {
  private Point center;
  private int radius;
  
  public Circle(Point center, int radius) {
    this.center = center;
    this.radius = radius;
  }
  
  public Point getLocation() {
    return center;
  }
  
  public void setLocation(Point center) {
    this.center = center;
  }
  
  public int getRadius() {
    return radius;
  }
  
  public void draw(Graphics g) {
    g.drawOval(center.x - radius, center.y - radius, radius * 2, radius * 2);
  }
}
{% endhighlight %}

We may translate this directly into Scala as follows:
{% highlight scala %}
trait Shape {
  def getLocation: Point
  def setLocation(center: Point): Unit
  def draw(g: Graphics): Unit
}

class Square(private var center: Point, val width: Int) extends Shape {
  def getLocation = center
  
  def setLocation(center: Point) {
    this.center = center
  }
  
  def draw(g: Graphics) {
    g.drawRect(center.x - width/2, center.y - width/2, width, width)
  }
}

// Circle is similar
{% endhighlight %}
**Exercise:** Supply the Scala translation of the `Circle` class.

Except for a few changed keywords ("trait" instead of "interface", "extends" instead of "implements") and the slightly different syntax for defining variables and functions (the "var", "val", and "def" keywords, and the type after instead of before each identifier), the only real difference here is how we set up the instance fields for the subclasses.  Scala supports an abbreviation for the common pattern of declaring simple fields and initializing them in the constructor -- the first line of the `Square` class definition says that there will be a private mutable field named `center` and a public read-only field named `width`, both to be initialized when the object is constructed.  Since `width` is public, there will automatically be an accessor method named `width` on `Square` objects, corresponding to the `getWidth()` in the Java version.

In fact, we may write this somewhat more idiomatically as follows:
{% highlight scala %}
trait Shape {
  def location: Point
  def location_=(center: Point): Unit
  def draw(g: Graphics): Unit
}

class Square(var location: Point, val width: Int) extends Shape {
  def draw(g: Graphics) {
    g.drawRect(location.x - width/2, location.y - width/2, width, width)
  }
}

// Circle is similar
{% endhighlight %}
The mutable field `location`, now public, will provide both the accessor `location` and the mutator `location_=` (which is called when there is an assignment to the `location` field: `mySquare.location = new Point(1, 2)` turns into the call `mySquare.location_=(new Point(1, 2))`), as promised in the `Shape` trait.

Here is a complete Scala program showing off these shapes:
<script src="http://gist.github.com/398723.js?file=ShapeTest.scala">
</script>

**Exercise:** Test this program, then add a `Circle` with radius 50 to the list of shapes.

Observe that the `main` method is defined in the *object* `ShapeTest`.  In Java, `main` would be a static method in a *class*.  Scala does away with the `static` keyword and replaces it with the more general concept of a "singleton" object: by declaring `ShapeTest` as an object, it creates a single-use class *and* constructs the only instance of that class in one step.  Scala allows the same name to be used for both a class and an object, which is then referred to as the class' "companion" object.  The companion object can hold data and methods that are shared among all instances of the class, just like static fields and methods in Java.  A common pattern is to define a "factory method" in the companion object -- a method which may be used to construct instances of the class:
<script src="http://gist.github.com/398750.js?file=Square.scala">
</script>
Now the call `Square.apply(new Point(1, 2), 3)` will return a newly-constructed `Square` object.  The `apply` method is special, because it will be called whenever the object is used as the name of a function itself: `Square(new Point(1, 2), 3)` is equivalent.  This might seem to be a lot of machinery just to avoid typing the word "new", but it is an important part of making algebraic data types fit into the language naturally (see below).

## Inheritance and Mixins

Suppose we wish to extend our shape example so that there are several kinds of shapes.  Ordinary shapes are centered at the origin; they can draw themselves and tell you their area, but that's all.  *Locatable* shapes add the ability to move to other locations.  Eventually we will have other categories such as *colorable* shapes, but let's work up to that.

Here is a possible implementation in Java:
{% highlight java %}
public interface Shape {
  void draw(Graphics g);
  double getArea();
}

/** Ordinary (non-locatable) squares
 */
public class Square implements Shape {
  private int width;
  
  public Square(int width) {
    this.width = width;
  }
  
  public void draw(Graphics g) {
    g.drawRect(-width/2, -width/2, width, width);
  }
  
  public double getArea() {
    return width * width;
  }
}

// Circle is similar

public interface LocatableShape extends Shape {
  Point getLocation();
  void setLocation(Point center);
}

public class LocatableSquare extends Square implements LocatableShape {
  private Point center;
  
  public LocatableSquare(Point center, int width) {
    super(width);
    this.center = center;
  }
  
  public Point getLocation() {
    return center;
  }
  
  public void setLocation(Point center) {
    this.center = center;
  }
  
  @Override
  public void draw(Graphics g) {
    Graphics gshift = g.create();         // Make a copy of g
    gshift.translate(center.x, center.y); // with origin at center
    super.draw(gshift);
  }
}

// LocatableCircle is similar
{% endhighlight %}

**Exercise:** Write out the code for `Circle` and `LocatableCircle`.

Notice that there is nothing specific to squares in the `LocatableSquare` class; it inherits all of its square-like behavior from the `Square` class.  In fact, all of the code in `LocatableSquare` is about managing the shape's location; you should have written virtually the same code again for `LocatableCircle`.  According to the DRY ("Don't Repeat Yourself") principle, this duplication is bad -- it's extra work (imagine what you would have to do if we had dozens of ordinary shapes to make locatable), and it's a maintenance problem to find and fix all of the copies in case the common code needs to be changed.

What we would like is if all of the location-specific code would come along with the `LocatableShape` interface.  Unfortunately, Java doesn't allow code (fields or method bodies) in interfaces.  A Java class is allowed to inherit behavior from only one superclass, although it may implement the methods of many interfaces.  We have to choose whether `LocatableSquare` inherits from `Square` and implements `LocatableShape` (as above), or whether it should extend some `AbstractLocatableShape` class and duplicate the square behavior.  Either way, there will be some repeated code.

Scala solves this problem through the mechanism of *mixin traits*.  A mixin trait is essentially an interface plus the associated code.  When a subclass is defined using the trait, the code is "mixed in" along with the code inherited from the superclass.  Here is how the above example looks in Scala:
{% highlight scala %}
trait Shape {
  def draw(g: Graphics): Unit
  def getArea: Double
}

class Square(width: Int) extends Shape {
  def draw(g: Graphics) {
    g.drawRect(-width/2, -width/2, width, width)
  }
  
  def getArea = width * width
}

// Circle is similar

trait LocatableShape extends Shape {
  def location: Point
  def location_=(center: Point): Unit
  
  abstract override def draw(g: Graphics) {
    val gshift = g.create
    gshift.translate(location.x, location.y)
    super.draw(gshift)
  }
}

class LocatableSquare(var location: Point, width: Int) extends
  Square(width) with LocatableShape

// LocatableCircle is similar
{% endhighlight %}
The modifier `abstract override` on the `draw` method in `LocatableShape` indicates that the class being mixed into must provide a `draw` method, but that it will be replaced by this new version.  The original `draw` method may still be called using the `super.draw` reference.

**Exercise:** Fill in the missing `Circle` and `LocatableCircle` classes, then test these new shape classes in the `ShapeTest` program above (the list of shapes should now have type `List[LocatableShape]`).

**Exercise:** Follow the pattern of `LocatableShape` and define a new trait `ColorableShape`.  Here is an example of AWT code to draw a red circle:
{% highlight scala %}
g.setColor(Color.RED)
g.drawOval(-radius, -radius, radius * 2, radius * 2)
{% endhighlight %}
Now extend the `ShapeTest` program to use colorable locatable shapes.  You ought to be able to define such a class as follows:
{% highlight scala %}
class ColorableLocatableSquare(var color: Color, var location: Point, width: Int) extends
  Square(width) with ColorableShape with LocatableShape
{% endhighlight %}

## Algebraic Data Types

The essence of the object-oriented paradigm is that each object has code associated with it, so that the object "knows" how to perform various operations on its data.  Two objects might respond to the same set of requests (*i.e.*, have the same interface), but they might perform their jobs in radically different ways.  Continuing the shape example, circles and squares both have `draw` methods, but they produce different shapes on the screen.  One advantage of this is that it is easy to introduce new varieties of objects (*e.g.*, triangles) without modifying existing code.  As a result, good OO style avoids code which is conditional on what kind of object one has -- a program that depended on shapes being either squares or circles would break if you introduced triangles.

In fact, some people advocate OO programming without using `if` statements at all!  Indeed, the Smalltalk language doesn't *have* an `if` statement.  It does have `true` and `false` values, however.  These values are objects, just like everything else in Smalltalk, and they support several methods (in Smalltalk terms, they "respond to messages") including `ifTrue:ifFalse:`, which takes two arguments and evaluates the corresponding one depending on whether the object is `true` or `false`.  Here is equivalent code in Scala (the `&&` operator will be used below):
<script src="http://gist.github.com/400128.js?file=gistfile1.txt">
</script>
We will learn more about the parameter type `=> T` later; the effect here is that `ifThenElse` takes a pair of unevaluated chunks of code (each of which would produce a result of type `T`), and only evaluates the one corresponding to the object's truth value.  When using this code, note that the interdependence between the `Bool` trait and the `False` object require that this file be compiled by `scalac`; it will not work directly in the REPL.

Here is an example of using this:
{% highlight scala %}
def test(b: Bool) {
  b.ifThenElse(println("it's true"), println("it's false"))
}

test(True)  // prints "it's true"
test(False) // prints "it's false"
{% endhighlight %}
Note that there are no conditionals in this code at all; the choice of whether to print "it's true" or "it's false" is entirely up to the `Bool` objects.  Also, as promised, it is easy to add a new kind of `Bool` object in this setting.  Suppose we wanted to model a "fuzzy" truth value (as in [Fuzzy Logic](http://mathworld.wolfram.com/FuzzyLogic.html)), which has a probability of truth between 0 and 1.  The following is one way to do this:
{% highlight scala %}
class Fuzzy(probability: Double) extends Bool {
  def ifThenElse(trueClause: => Unit, falseClause: => Unit) =
    if (Math.random < probability) trueClause else falseClause
}
{% endhighlight %}
(We're forced to use an `if` statement here because the `<` operator returns a `Boolean` value rather than one of our `Bool` objects....) Now, when we evaluate `test(new Fuzzy(0.75))`, it should print "it's true" about three-fourths of the time.

**Exercise:** Check that the `&&` method provides a logical AND operation on `Bool` values; that is, evaluate expressions such as `True && False` and see how it works.  Now define an analogous method for logical OR.  As a challenge, determine whether these functions behave reasonably on fuzzy truth values.

All of this is a prelude to looking at algebraic data types.  Where the OO paradigm emphasizes the association of code with each object, the functional paradigm does the opposite: it gives the code equal footing with the data, by treating functions as first-class values.  As a result, there is almost a dual nature between the two paradigms.  In OO, it is easy to add new kinds of objects without modifying existing code, but if you want to add a new method on those objects you often have to add it to every class (think of what it would take to add a `getPerimeter` method to the shape classes).  By contrast, we will see that the use of algebraic data types in the functional paradigm makes it easy to add new operations, but more difficult to extend the kinds of values those operations work with.  Nonetheless, there are substantial similarities between the two approaches, and one of the nice features of a multi-paradigm language like Scala is that you can work in either style, or a mixture of them.

An algebraic data type is specified by giving a set of *constructors*.  Each constructor takes a list of zero or more data values as parameters, and represents one of the ways to create a value of the data type.  For example, consider the type of lists of integers.  A list may be empty, or it may consist of an integer at the head, plus a tail which is another list of integers.  Here is how this might be expressed (though not quite idiomatically) in the functional language Haskell:
{% highlight haskell %}
data List = Nil
          | Cons(Int, List)
{% endhighlight %}
The constructors here are `Nil`, which needs no parameters to create an empty list, and `Cons`, which takes an `Int` and a `List` (the head and the tail).

Scala's syntax for algebraic data types is not as concise as Haskell's, in exchange for the flexibility of mixing the functional and object-oriented paradigms.  Here is one way to express integer lists in Scala:
{% highlight scala %}
trait List
object Nil extends List
class Cons(val head: Int, val tail: List) extends List
{% endhighlight %}
Surprise!  It looks just like the OO class hierarchies we have already seen, except there are no member functions, and the only instance variables are the (immutable) constructor parameters.  In fact, these two points characterize the standard use of algebraic data types in functional languages: all of the contained data is supplied to the constructors (and thereafter remains unmodified), and operations on the data are defined as external functions.  In the Scala version, we may extract the data from a constructed object using accessor methods (`head` and `tail` in the above example), but the more common functional style is to access the data through "pattern matching."  Suppose we want to write a function `first` in Haskell which takes an integer list and returns either the head of the list, or 0 if the list is empty.  Here is one way to do this:
{% highlight haskell %}
first(list) = case list of
                Nil -> 0
                Cons(head, tail) -> head
{% endhighlight %}
It is more common in Haskell to combine pattern matching with function definition, which fits nicely with an algebraic notion of evaluation by substituting equals for equals, and also with the common practice in functional languages of recursive function definitions (mirroring the recursive data type definitions).  Here is the `first` function again, and a recursive function to sum the contents of an integer list:
{% highlight haskell %}
first(Nil) = 0
first(Cons(head, tail)) = head

sum(Nil) = 0
sum(Cons(head, tail)) = head + sum(tail)
{% endhighlight %}
(For clarity, more parentheses have been inserted than would be typical in idiomatic Haskell.)

**Exercise:** Complete the following evaluation process and check that the `sum` function works as expected:
{% highlight haskell %}
sum(Cons(1, Cons(2, Cons(3, Nil))))
 = 1 + sum(Cons(2, Cons(3, Nil)))
 = ...
{% endhighlight %}

Before translating these functions into Scala, we need to add some modifiers to the definitions given above for the `List` trait and its subclasses.  These modifiers tell Scala to generate some extra code for us automatically, to support pattern matching better:
{% highlight scala %}
sealed trait List
case object Nil extends List
case class Cons(head: Int, tail: List) extends List
{% endhighlight %}
The modifier `sealed` notifies Scala that `List` will only have the following subclasses; this lets it generate more efficient code in pattern matching because it can check that we have covered all of the possible varieties of `List`.  The modifier `case` causes several things to happen: the parameters of `Cons` are automatically declared as `val`s, a factory method is generated so that we can create a new `Cons` object without having to say `new`, and further methods are generated to handle operations such as equality testing between `List`s, converting a `List` to a string, and extracting the fields of a `Cons` object when doing a pattern match.  We could write all of these ourselves (as we did with the factory method earlier), but it is very convenient to have the compiler do this tedious work for us.

Unfortunately, Scala does not support the pattern matching form of function definition, so the idiom we will use is the `match` expression, which corresponds to Haskell's `case` statement.  Here are Scala versions of `first` and `sum`:
{% highlight scala %}
def first(list: List): Int = list match {
  case Nil => 0
  case Cons(head, tail) => head
}

def sum(list: List): Int = list match {
  case Nil => 0
  case Cons(head, tail) => head + sum(tail)
}
{% endhighlight %}
**Exercise:** Enter the definitions of `List`, `Nil`, and `Cons` in the REPL, then check that the `first` and `sum` functions work as expected.  Now write a function `last` which returns the *final* element of a `List`, or 0 if empty.  Hint: patterns may involve several constructors, such as `Cons(a, Cons(b, Nil))` -- this will match a two-element list, binding the elements to the names `a` and `b`.  The Scala compiler will tell you if your case patterns don't cover all of the possible values.

For comparison, here is an algebraic data type version of the boolean example:
{% highlight scala %}
sealed trait Bool
case object True extends Bool
case object False extends Bool

def ifThenElse[T](b: Bool, trueClause: => T, falseClause: => T): T = b match {
  case True => trueClause
  case False => falseClause
}
{% endhighlight %}
This particularly simple form of algebraic data type is known as an "enumeration" type -- the type consists of a (small) set of values, and functions are frequently defined by cases on those values.


## Type Parameters

Something about type parameters
