package parsing.backtrack

import scala.collection.mutable
import scala.util.Try

/**
  * Created by hongdi.ren.
  */

abstract class Parser(input: Lexer) {

  protected val lookahead: mutable.ArrayBuffer[Token] = mutable.ArrayBuffer()
  protected val markers: mutable.ArrayStack[Int] = mutable.ArrayStack()

  protected var p: Int = 0

  def mark(): Int = {
    markers.push(p)
    p
  }

  def release(): Unit = {
    val m = markers.pop()
    seek(m)
  }

  def seek(index: Int): Unit = {
    p = index
  }

  def isSpeculating(): Boolean = markers.nonEmpty

  def consume(): Unit = {
    p = p + 1
    if (p == lookahead.size && !isSpeculating()) {
      p = 0
      lookahead.clear()
    }
    sync(1)
  }

  def LT(i: Int): Token = {
    sync(i)
    lookahead(p + i - 1)
  }

  def sync(i: Int): Unit = {
    if (p + i - 1 > (lookahead.size - 1))
      fill(p + i - 1 - (lookahead.size - 1))
  }

  def fill(i: Int): Unit = {
    (1 to i).foreach(_ => lookahead += input.nextToken())
  }

  def LA(i: Int): Int = LT(i).`type`

  def `match`(x: Int): Unit = {
    if (LA(1) == x) consume()
    else throw new IllegalStateException(s"expecting ${input.getTokenName(x)}; found ${LT(1)}")
  }
}

class BacktrackParser(input: Lexer) extends Parser(input) {

  //stat: list EOF | assign EOF ;
  def stat(): Unit = {
    if (speculate_stat_alt1()) {
      list()
      `match`(Lexer.EOF_TYPE)
    } else if (speculate_stat_alt2()) {
      assign()
      `match`(Lexer.EOF_TYPE)
    } else throw new IllegalStateException(s"expecting stat found ${LT(1)}")
  }

  def speculate_stat_alt1(): Boolean = {
    mark()
    val t = Try {
      list()
      `match`(Lexer.EOF_TYPE)
    }
    release()
    t.isSuccess
  }

  def speculate_stat_alt2(): Boolean = {
    mark()
    val t = Try {
      assign()
      `match`(Lexer.EOF_TYPE)
    }
    release()
    t.isSuccess
  }

  def assign(): Unit = {
    list()
    `match`(BacktrackLexer.EQUALS)
    list()
  }

  //list : '[' elements ']' ;
  def list(): Unit = {
    `match`(BacktrackLexer.LBRACK)
    elements()
    `match`(BacktrackLexer.RBRACK)
  }

  //  elements : element (',' element)*;
  def elements(): Unit = {
    element()
    while (LA(1) == BacktrackLexer.COMMA) {
      `match`(BacktrackLexer.COMMA)
      element()
    }
  }

  //  element : NAME '=' NAME | NAME | list;
  def element(): Unit = {
    if (LA(1) == BacktrackLexer.NAME && LA(2) == BacktrackLexer.EQUALS) {
      `match`(BacktrackLexer.NAME)
      `match`(BacktrackLexer.EQUALS)
      `match`(BacktrackLexer.NAME)
    }
    else if (LA(1) == BacktrackLexer.NAME) `match`(BacktrackLexer.NAME)
    else if (LA(1) == BacktrackLexer.LBRACK) list()
    else throw new IllegalStateException(s"expecting name or list; found ${LT(1)}")
  }
}
