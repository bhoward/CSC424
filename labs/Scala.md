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
  void setLocation(Point p);
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
  def setLocation(p: Point): Unit
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
  def location_=(p: Point): Unit
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

Something about mixin traits

Something about algebraic data types

Something about type parameters