---
layout: default
title: CSC 424: Programming Languages
---
# CSC 424: Programming Languages
This site collects resources for use in CSC 424.  For information specific to a particular semester, consult the associated Moodle page:

* [Fall 2010](http://moodle.depauw.edu/course/view.php?id=6949)

## Schedule
Week | Topics                    | Readings                      | Assignments
---- | ------------------------- | ----------------------------- | -----------
1    | Intro                     | Scott: Ch. 1                  | [Lab One: Scala Overview](labs/Scala.html)
2    | Syntax                    | Scott: Ch. 2, only Sect. 1    | [Lab Two: Parsing](labs/Parsing.html)
3    | Names and Bindings        | Scott: Ch. 3, except Sect. 4  | Lab Three: Expression Language
4    | Control Flow              | Scott: Ch. 6, Sects. 1-6      | Lab Four: Defining Control Structures
5    | Data Types                | Scott: Ch. 7                  | Lab Five: Type Checking
6    | Control Abstraction       | Scott: Ch. 8                  | Lab Six: Function Language
7    | Midterm Exam              |                               |
8    | Data Abstraction and OOP  | Scott: Ch. 9                  | Lab Seven: Design Patterns
9    | Functional Programming    | Scott: Ch. 10                 | Lab Eight: Higher-Order Control Structures
10   | Logic Programming         | Scott: Ch. 11                 | Lab Nine: Prolog
11   | Concurrency               | Scott: Ch. 6, Sect. 7; Ch. 12 | [Lab Ten: Approaches to Concurrency](labs/Concurrency.html)
12   | Scripting Languages       | Scott: Ch. 13                 | Lab Eleven: JavaScript
13   | Domain-Specific Languages |                               | Lab Twelve: Internal and External DSLs
14   | Wrap-up and Presentations |                               |

## Readings
### Textbooks
There are *many* textbooks covering the area of programming languages in general (to say nothing of the books that focus on specific languages); here are links to a few of the better ones (several of which are available online):

* Michael Scott, [http://www.cs.rochester.edu/~scott/pragmatics/ Programming Language Pragmatics]
* Adam Webber, [http://www.webber-labs.com/mpl.html Modern Programming Languages]
* John Mitchell, [http://theory.stanford.edu/people/jcm/books/cpl-teaching.html Concepts in Programming Languages]
* Daniel Friedman and Mitchell Wand, [http://www.eopl3.com/ Essentials of Programming Languages]
* Allen Tucker and Robert Noonan, [http://www.bowdoin.edu/~allen/pl/ Programming Languages: Principles and Paradigms]
* Maurizio Gabbrielli and Simone Martini, [http://www.springer.com/computer/book/978-1-84882-913-8 Programming Languages: Principles and Paradigms]
* Franklyn Turbak and David Gifford, [http://mitpress.mit.edu/catalog/item/default.asp?ttype=2&tid=11656 Design Concepts in Programming Languages]
* Shriram Krishnamurthi, [http://www.cs.brown.edu/~sk/Publications/Books/ProgLangs/ Programming Languages: Application and Interpretation]
* Raphael Finkel, [http://c2.com/cgi/wiki?AdvancedProgrammingLanguageDesign Advanced Programming Language Design]
* Mike Grant, Zachary Palmer, and Scott Smith, [http://www.cs.jhu.edu/~scott/pl/book/dist/index.html Programming Languages]
* Mordechai Ben-Ari, [http://stwww.weizmann.ac.il/g-cs/benari/books/#upl Understanding Programming Languages]
* Peter Van Roy and Seif Haridi, [http://www.info.ucl.ac.be/~pvr/book.html Concepts, Techniques, and Models of Computer Programming]
* Harold Abelson and Gerald Jay Sussman [http://mitpress.mit.edu/sicp/ Structure and Interpretation of Computer Programs] -- these last two are more about programming in general, but contain many deep insights about the design of programming languages

One of the common features of all of these texts is that they focus on the concepts behind various languages and paradigms, rather than simply presenting a catalog of different language features.

### Articles
* Krste Asanovic, *et al.*, [http://www.eecs.berkeley.edu/Pubs/TechRpts/2006/EECS-2006-183.pdf The Landscape of Parallel Computing Research: A View from Berkeley]
* John Backus, [http://www.thocp.net/biographies/papers/backus_turingaward_lecture.pdf Can Programming Be Liberated from the von Neumann Style?]
* Kent Beck and Ward Cunningham, [http://c2.com/doc/oopsla89/paper.html A Laboratory For Teaching Object-Oriented Thinking]
* Joseph Bergin, [http://csis.pace.edu/~bergin/patterns/persongender.html Moving Toward Object-Oriented Programming and Patterns]
* Luca Cardelli and Peter Wegner, [http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.70.8559&rep=rep1&type=pdf On Understanding Types, Data Abstraction, and Polymorphism]
* Edgar Codd, [http://www.cs.berkeley.edu/~christos/classics/Codd72a.pdf Relational Completeness of Data Base Sublanguages]
* William Cook, [http://userweb.cs.utexas.edu/~wcook/Drafts/2009/essay.pdf On Understanding Data Abstraction, Revisited]
* Jeffrey Dean and Sanjay Ghemawat, [http://labs.google.com/papers/mapreduce.html MapReduce: Simplified Data Processing on Large Clusters]
* Edsgar Dijkstra, [http://userweb.cs.utexas.edu/~EWD/transcriptions/EWD03xx/EWD340.html The Humble Programmer]
* Edsgar Dijkstra, [http://userweb.cs.utexas.edu/users/EWD/ewd02xx/EWD215.PDF A Case against the GO TO Statement] (also known as "Go-to statement considered harmful")
* Edsgar Dijkstra, [http://www.cs.utexas.edu/users/EWD/ewd02xx/EWD249.PDF Notes on Structured Programming]
* Ulrich Drepper, [http://people.redhat.com/drepper/cpumemory.pdf What Every Programmer Should Know About Memory]
* Richard Gabriel, [http://www.dreamsongs.com/WIB.html Lisp: Good News, Bad News, How to Win Big]
* David Goldberg, [http://docs.sun.com/source/806-3568/ncg_goldberg.html What Every Computer Scientist Should Know About Floating-Point Arithmetic]
* James Gosling and Henry McGilton, [http://java.sun.com/docs/white/langenv/ White Paper: The Java Language Environment]
* Tony Hoare, [http://www.cs.berkeley.edu/~necula/cs263/handouts/hoarehints.pdf Hints on Programming Language Design]
* John Hughes, [http://www.cs.chalmers.se/~rjmh/Papers/whyfp.html Why Functional Programming Matters]
* John Knight and Nancy Leveson, [http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.29.363&rep=rep1&type=pdf An Experimental Evaluation of the Assumption of Independence in Multiversion Programming]
* Donald Knuth, [http://pplab.snu.ac.kr/courses/adv_pl05/papers/p261-knuth.pdf Structured Programming with go to Statements]
* Peter Landin, [http://www.thecorememory.com/Next_700.pdf The Next 700 Programming Languages]
* John McCarthy, [http://www-formal.stanford.edu/jmc/recursive/recursive.html Recursive Functions of Symbolic Expressions and Their Computation by Machine, Part I]
* James Noble, [http://www.laputan.org/pub/patterns/noble/noble.pdf Arguments and Results]
* David Parnas, [http://sunnyday.mit.edu/16.355/parnas-criteria.html On the Criteria to be Used in Decomposing Systems into Modules]
* Simon Peyton Jones, [http://research.microsoft.com/en-us/um/people/simonpj/papers/stm/beautiful.pdf Beautiful Concurrency]
* Joel Spolsky, [http://www.joelonsoftware.com/printerFriendly/articles/Unicode.html The Absolute Minimum Every Software Developer Absolutely, Positively Must Know About Unicode and Character Sets (No Excuses!)]
* Guy Steele, [http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.10.1635&rep=rep1&type=pdf Growing a Language] ([http://video.google.com/videoplay?docid=-8860158196198824415 video])
* Herb Sutter, [http://www.gotw.ca/publications/concurrency-ddj.htm The Free Lunch Is  Over: A Fundamental Turn Toward Concurrency in Software]
* Ken Thompson, [http://cm.bell-labs.com/who/ken/trust.html Reflections on Trusting Trust]
* David Ungar and Randall Smith, [http://research.sun.com/features/tenyears/volcd/papers/6Ungar.pdf Programming as an Experience: the Inspiration for Self]
* Peter Van Roy, [http://www.info.ucl.ac.be/~pvr/VanRoyChapter.pdf Programming Paradigms for Dummies: What Every Programmer Should Know]
* Jim Waldo, Geoff Wyant, Ann Wollrath, and Sam Kendall, [http://research.sun.com/techrep/1994/abstract-29.html A Note On Distributed Computing]

Part of this list comes from a 2009 blog post by Michael Feathers, [http://blog.objectmentor.com/articles/2009/02/26/10-papers-every-programmer-should-read-at-least-twice 10 Papers Every Programmer Should Read (At Least Twice)].  The comments on that post suggest many additional worthwhile papers.
