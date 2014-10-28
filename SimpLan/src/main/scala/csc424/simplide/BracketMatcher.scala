package csc424.simplide

// BracketMatcher.scala
// Based on Java code written by Joshua Engel:
//   http://www.informit.com/articles/article.aspx?p=31204

import java.awt.Color
import javax.swing.event.{ CaretListener, CaretEvent }
import javax.swing.text._

class BracketMatcher extends CaretListener {
  val goodPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW)
  val badPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.MAGENTA)

  var highlighter: Option[Highlighter] = None
  var start: Option[AnyRef] = None
  var end: Option[AnyRef] = None

  def clearHighlights() {
    for (h <- highlighter) {
      for (tag <- start) h.removeHighlight(tag)
      for (tag <- end) h.removeHighlight(tag)
      start = None
      end = None
      highlighter = None
    }
  }

  def getCharAt(doc: Document, pos: Int): Char = {
    doc.getText(pos, 1).charAt(0)
  }

  // This does nothing about ignoring brackets in quotes or comments...
  def findMatchingBracket(doc: Document, pos: Int): Int = {
    var bracketCount = 0
    var i = pos
    while ((i == pos || bracketCount != 0) && i >= 0) {
      getCharAt(doc, i) match {
        case '(' | '{' | '[' => bracketCount -= 1
        case ')' | '}' | ']' => bracketCount += 1
        case _ => {}
      }
      if (bracketCount != 0) i -= 1
    }

    i
  }

  def caretUpdate(e: CaretEvent) {
    clearHighlights()

    val source = e.getSource.asInstanceOf[JTextComponent]
    val doc = source.getDocument

    val closePos = e.getDot - 1
    if (closePos < 0) return

    val closeCh = getCharAt(doc, closePos)
    closeCh match {
      case ')' | '}' | ']' =>
        highlighter = Some(source.getHighlighter)
        val openPos = findMatchingBracket(doc, closePos)
        if (openPos >= 0) {
          val openCh = getCharAt(doc, openPos)
          if ((openCh == '(' && closeCh == ')') ||
            (openCh == '{' && closeCh == '}') ||
            (openCh == '[' && closeCh == ']')) {
            start = Some(highlighter.get.addHighlight(openPos, openPos + 1, goodPainter))
            end = Some(highlighter.get.addHighlight(closePos, closePos + 1, goodPainter))
          } else {
            start = Some(highlighter.get.addHighlight(openPos, openPos + 1, badPainter))
            end = Some(highlighter.get.addHighlight(closePos, closePos + 1, badPainter))
          }
        } else {
          end = Some(highlighter.get.addHighlight(closePos, closePos + 1, badPainter))
        }
      case _ => {}
    }
  }
}
