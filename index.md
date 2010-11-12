---
layout: default
title: CSC 424 Programming Languages
---
# CSC 424: Programming Languages
This site collects resources for use in CSC 424.  For information specific to a particular semester, consult the associated Moodle page:

* [Fall 2010](http://moodle.depauw.edu/course/view.php?id=6949)

## Schedule
Week | Topics                    | Readings                      | Assignments
---- | ------------------------- | ----------------------------- | -----------
1    | Intro                     | Scott: Ch. 1                  | [Lab One: Scala Overview](labs/Scala.html)
2    | Syntax                    | Scott: Ch. 2, only Sect. 1    | [Lab Two: Parsing](labs/Parsing.html)
3    | Names and Bindings        | Scott: Ch. 3, except Sect. 4  | [Lab Three: Expression Language](labs/Expression.html)
4    | Control Flow              | Scott: Ch. 6, Sects. 1-6      | [Lab Four: Defining Control Structures](labs/Control.html)
5    | Data Types                | Scott: Ch. 7                  | [Lab Five: Type Checking](labs/Types.html)
6    | Control Abstraction       | Scott: Ch. 8                  | [Lab Six: Function Language](labs/Function.html)
7    | Midterm Exam              | --                            | --
8    | Data Abstraction and OOP  | Scott: Ch. 9                  | [Lab Seven: Design Patterns](labs/Patterns.html)
9    | Functional Programming    | Scott: Ch. 10                 | [Lab Eight: Higher-Order Control Structures](labs/HOCS.html)
10   | Logic Programming         | Scott: Ch. 11                 | [Lab Nine: Prolog](labs/Prolog.html)
11   | Concurrency               | Scott: Ch. 6, Sect. 7; Ch. 12 | [Lab Ten: Approaches to Concurrency](labs/Concurrency.html)
12   | Scripting Languages       | Scott: Ch. 13                 | [Lab Eleven: JavaScript](labs/JavaScript.html)
13   | Domain-Specific Languages | --                            | [Lab Twelve: Internal and External DSLs](labs/DSL.html)
14   | Wrap-up and Presentations | --                            | --

## Readings
### Textbooks
There are *many* textbooks covering the area of programming languages in general (to say nothing of the books that focus on specific languages); here are links to a few of the better ones (several of which are available online):

* Michael Scott, [Programming Language Pragmatics](http://www.cs.rochester.edu/~scott/pragmatics/)
* Adam Webber, [Modern Programming Languages](http://www.webber-labs.com/mpl.html)
* John Mitchell, [Concepts in Programming Languages](http://theory.stanford.edu/people/jcm/books/cpl-teaching.html)
* Daniel Friedman and Mitchell Wand, [Essentials of Programming Languages](http://www.eopl3.com/)
* Allen Tucker and Robert Noonan, [Programming Languages: Principles and Paradigms](http://www.bowdoin.edu/~allen/pl/)
* Maurizio Gabbrielli and Simone Martini, [Programming Languages: Principles and Paradigms](http://www.springer.com/computer/book/978-1-84882-913-8)
* Franklyn Turbak and David Gifford, [Design Concepts in Programming Languages](http://mitpress.mit.edu/catalog/item/default.asp?ttype=2&tid=11656)
* Shriram Krishnamurthi, [Programming Languages: Application and Interpretation](http://www.cs.brown.edu/~sk/Publications/Books/ProgLangs/)
* Raphael Finkel, [Advanced Programming Language Design](http://c2.com/cgi/wiki?AdvancedProgrammingLanguageDesign)
* Mike Grant, Zachary Palmer, and Scott Smith, [Programming Languages](http://www.cs.jhu.edu/~scott/pl/book/dist/index.html)
* Mordechai Ben-Ari, [Understanding Programming Languages](http://stwww.weizmann.ac.il/g-cs/benari/books/#upl)
* Peter Van Roy and Seif Haridi, [Concepts, Techniques, and Models of Computer Programming](http://www.info.ucl.ac.be/~pvr/book.html)
* Harold Abelson and Gerald Jay Sussman, [Structure and Interpretation of Computer Programs](http://mitpress.mit.edu/sicp/) -- these last two are more about programming in general, but contain many deep insights about the design of programming languages

One of the common features of all of these texts is that they focus on the concepts behind various languages and paradigms, rather than simply presenting a catalog of different language features.

### Articles
* Joe Armstrong, [A History of Erlang](http://www.cs.chalmers.se/Cs/Grundutb/Kurser/ppxt/HT2007/general/languages/armstrong-erlang_history.pdf)
* Krste Asanovic, *et al.*, [The Landscape of Parallel Computing Research: A View from Berkeley](http://www.eecs.berkeley.edu/Pubs/TechRpts/2006/EECS-2006-183.pdf)
* John Backus, [Can Programming Be Liberated from the von Neumann Style?](http://www.thocp.net/biographies/papers/backus_turingaward_lecture.pdf)
* Kent Beck and Ward Cunningham, [A Laboratory For Teaching Object-Oriented Thinking](http://c2.com/doc/oopsla89/paper.html)
* Joseph Bergin, [Moving Toward Object-Oriented Programming and Patterns](http://csis.pace.edu/~bergin/patterns/persongender.html)
* Luca Cardelli and Peter Wegner, [On Understanding Types, Data Abstraction, and Polymorphism](http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.70.8559&rep=rep1&type=pdf)
* Edgar Codd, [Relational Completeness of Data Base Sublanguages](http://www.cs.berkeley.edu/~christos/classics/Codd72a.pdf)
* William Cook, [On Understanding Data Abstraction, Revisited](http://userweb.cs.utexas.edu/~wcook/Drafts/2009/essay.pdf)
* Jeffrey Dean and Sanjay Ghemawat, [MapReduce: Simplified Data Processing on Large Clusters](http://labs.google.com/papers/mapreduce.html)
* Edsgar Dijkstra, [The Humble Programmer](http://userweb.cs.utexas.edu/~EWD/transcriptions/EWD03xx/EWD340.html)
* Edsgar Dijkstra, [Go To Statement Considered Harmful](http://paramount.www.ecn.purdue.edu/ParaMount/papers/dijkstra68goto.pdf)
* Edsgar Dijkstra, [Notes on Structured Programming](http://www.cs.utexas.edu/users/EWD/ewd02xx/EWD249.PDF)
* Ulrich Drepper, [What Every Programmer Should Know About Memory](http://people.redhat.com/drepper/cpumemory.pdf)
* Richard Gabriel, [Lisp: Good News, Bad News, How to Win Big](http://www.dreamsongs.com/WIB.html)
* David Goldberg, [What Every Computer Scientist Should Know About Floating-Point Arithmetic](http://docs.sun.com/source/806-3568/ncg_goldberg.html)
* James Gosling and Henry McGilton, [White Paper: The Java Language Environment](http://java.sun.com/docs/white/langenv/)
* Tony Hoare, [Hints on Programming Language Design](http://www.cs.berkeley.edu/~necula/cs263/handouts/hoarehints.pdf)
* John Hughes, [Why Functional Programming Matters](http://www.cs.kent.ac.uk/people/staff/dat/miranda/whyfp90.pdf)
* Daniel Ingalls, [Design Principles Behind Smalltalk](http://web.archive.org/web/20070213165045/users.ipa.net/~dwighth/smalltalk/byte_aug81/design_principles_behind_smalltalk.html)
* James Iry, [A Brief, Incomplete, and Mostly Wrong History of Programming Languages](http://james-iry.blogspot.com/2009/05/brief-incomplete-and-mostly-wrong.html)
* John Knight and Nancy Leveson, [An Experimental Evaluation of the Assumption of Independence in Multiversion Programming](http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.29.363&rep=rep1&type=pdf)
* Donald Knuth, [Structured Programming with go to Statements](http://pplab.snu.ac.kr/courses/adv_pl05/papers/p261-knuth.pdf)
* Peter Landin, [The Next 700 Programming Languages](http://www.thecorememory.com/Next_700.pdf)
* Robert Martin, [The Open-Closed Principle](http://www.objectmentor.com/resources/articles/ocp.pdf)
* John McCarthy, [Recursive Functions of Symbolic Expressions and Their Computation by Machine, Part I](http://www-formal.stanford.edu/jmc/recursive/recursive.html)
* James Noble, [Arguments and Results](http://www.laputan.org/pub/patterns/noble/noble.pdf)
* David Parnas, [On the Criteria to be Used in Decomposing Systems into Modules](http://sunnyday.mit.edu/16.355/parnas-criteria.html)
* Simon Peyton Jones, [Beautiful Concurrency](http://research.microsoft.com/en-us/um/people/simonpj/papers/stm/beautiful.pdf)
* Simon Peyton Jones, [Tackling the Awkward Squad: Monadic Input/Output, Concurrency, Exceptions, and Foreign-Language Calls in Haskell](http://research.microsoft.com/en-us/um/people/simonpj/papers/marktoberdorf/mark.pdf)
* Joel Spolsky, [The Absolute Minimum Every Software Developer Absolutely, Positively Must Know About Unicode and Character Sets (No Excuses!)](http://www.joelonsoftware.com/printerFriendly/articles/Unicode.html)
* Guy Steele, [Growing a Language](http://www.cs.virginia.edu/~evans/cs655/readings/steele.pdf) ([video](http://video.google.com/videoplay?docid=-8860158196198824415))
* Herb Sutter, [The Free Lunch Is  Over: A Fundamental Turn Toward Concurrency in Software](http://www.gotw.ca/publications/concurrency-ddj.htm)
* Dave Thomas, [Functional Programming - Crossing the Chasm?](http://www.jot.fm/issues/issue_2009_07/column4/index.html)
* Ken Thompson, [Reflections on Trusting Trust](http://cm.bell-labs.com/who/ken/trust.html)
* David Ungar and Randall Smith, [Programming as an Experience: the Inspiration for Self](http://research.sun.com/features/tenyears/volcd/papers/6Ungar.pdf)
* Peter Van Roy, [Programming Paradigms for Dummies: What Every Programmer Should Know](http://www.info.ucl.ac.be/~pvr/VanRoyChapter.pdf)
* Jim Waldo, Geoff Wyant, Ann Wollrath, and Sam Kendall, [A Note On Distributed Computing](http://research.sun.com/techrep/1994/abstract-29.html)

Part of this list comes from a 2009 blog post by Michael Feathers, [10 Papers Every Programmer Should Read (At Least Twice)](http://blog.objectmentor.com/articles/2009/02/26/10-papers-every-programmer-should-read-at-least-twice).  The comments on that post suggest many additional worthwhile papers.
