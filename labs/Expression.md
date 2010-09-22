---
layout: default
title: Expression Language Lab
---
# The Expression Language

The purpose of this lab is to explore using Scala to implement a
simple parser and interpreter. The language we will implement is
very simple: a program is a single expression. Expressions can be
formed using the usual arithmetic operators and numeric literals.
In addition, expressions may contain `let` and `do` constructs;
these will allow us to explore some of the issues involved in binding
values to names.

Here is an example of a `let` expression:

    let
      val a = 1
      val b = 2
      val c = 3
    in a + b * c

This is evaluated by creating a local scope containing the given
bindings for `a`, `b`, and `c`, then the expression `a + b * c` is
evaluated in that scope. Scopes may be nested; an inner binding
will shadow an outer binding for the same identifier:

    let
      val a = 1
    in let
         val a = a + 1
         val b = a + 2
       in a * b

This expression evaluates to 6. Note that the outer binding is still
active while the inner bindings (of 2 to `a` and 3 to `b`) are being
created.

A `do` expression builds on these bindings by allowing a sequence of
statements (with side-effects) to be executed. Only mutable bindings
may be changed (just as in Scala, these are identifiers bound with
`var` instead of `val`). Here is an example:

    let
      var x = 0
    in do
      read x
      x = x * 2
      write x
    in 0

In this example, the result of the expression is 0, but evaluating it
has the side-effect of requesting a number, multiplying it by 2, and
printing the result on the console.

## Structure of the Interpreter

The project is in the `public/ExprLang` folder on the I: drive. It is
meant to be built with [SBT](http://code.google.com/p/simple-build-tool/),
the Simple Build Tool. Double-clicking the RUNSBT.BAT file in Windows should
start up an interactive session with SBT. At the prompt, you can issue
commands such as "clean", "update", "compile", "test", or "console".
A particularly useful mode is to execute the command "~test", which tells
SBT to re-run the tests whenever a source file is changed -- you can then
edit the source files (for example, with jEdit or WordPad), and check
whether all of the tests pass after saving your work.

The sources are in the subdirectories `src/main/scala/csc424` and
`src/test/scala/csc424`.

## Exercises

1. Familiarize yourself with the interpreter as-is. Do `clean` and `update`
    when you first start working, then do `test` to run the tests. By saying
    `console`, SBT will start up a Scala command line with the classes available.
    Try the following:
        import csc424._
        val src = "1 + 2 * 3"
        val ast = ExprLangParser(src).get
        ExprLangInterpreter.eval(ast, EmptyEnvironment)
    For convenience, you might want to define a function such as
        def eval(src: String) = {
          val ast = ExprLangParser(src).get
          ExprLangInterpreter.eval(ast, EmptyEnvironment)
        }
    Then you can test Expression Language programs by simply calling `eval`
    with the source as a (string) argument.
    Now try each of the example programs above.

2. Extend the parser, interpreter, and test suite to handle floating
point literals (we will do this together in class).

3. Extend the language with versions of the `read` and `write`
statements that take string literals: `read "prompt", x` will request
a value for `x` by printing the given prompt, while `write "message"`
will print the given message.

4. Add functions to the arithmetic expressions: `sqrt(x)`, `abs(x)`,
`min(x, y)`, etc.

5. Write a program in the Expression Language to read in three numbers
and print them out in sorted order.

6. Add `swap` and `swapif` statements. `swap x, y` should exchange
the values in the (mutable) variables `x` and `y`, while `swapif x, y`
should only perform the exchange if `x` is greater than `y`.

7. Repeat Exercise 5 using the `swap` and `swapif` statements.

For each step in the above, make sure you extend the tests as appropriate.
