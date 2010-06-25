---
layout: default
title: Parsing Lab
---
# Parsing

In addition to providing access to virtually any library of code written in Java, the standard Scala distribution also supplies a number of powerful libraries of its own.  In this lab, we will get some preliminary experience using one of these, which we will be using several more times this semester: the combinator parsing library.  The details of how the parsing combinators are implemented are somewhat involved, but their use is quite straightforward.

Examples:
* S-Expressions: ((a b c) (1 2)) => yes/no, then to List[Any](List("a", "b", "c"), List("1", "2")) (use Symbols?)
* Arithmetic Expressions: follow Scott, Ch. 2
* Regexs: stand-alone matching, including extractors (Odersky, pp. 501-504), and in RegexParsers
