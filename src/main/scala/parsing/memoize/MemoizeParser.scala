package parsing.memoize

import parsing.memoize.Parser._

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
      clearMemo()
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

  def alreadyParsedRule(memoization: mutable.Map[Int, Int]): Boolean = {
    memoization.get(index())
      .exists { i =>
        println(s"parsed list before at index ${index()}; skip ahead to token index $i:${lookahead(i).text}")
        if (i == FAILED) throw new IllegalStateException("previous parse failed")
        seek(i)
        true
      }
  }

  def memoize(memoization: mutable.Map[Int, Int], startTokenIndex: Int, succeed: Boolean): Unit = {
    val stopTokenIndex = if (!succeed) FAILED else index()
    memoization += ((startTokenIndex, stopTokenIndex))
  }

  def clearMemo(): Unit

  def index(): Int = p
}

object Parser {
  val FAILED: Int = -1
}

class MemoizeParser(input: Lexer) extends Parser(input) {

  val list_memo: mutable.Map[Int, Int] = mutable.Map()

  def clearMemo(): Unit = list_memo.clear()

  //stat: list EOF | assign EOF ;
  def stat(): Unit = {
    if (speculate_stat_alt1()) {
      println("predict alternative 1")
      list()
      `match`(Lexer.EOF_TYPE)
    } else if (speculate_stat_alt2()) {
      println("predict alternative 2")
      assign()
      `match`(Lexer.EOF_TYPE)
    } else throw new IllegalStateException(s"expecting stat found ${LT(1)}")
  }

  def speculate_stat_alt1(): Boolean = {
    println("attempt alternative 1")
    mark()
    val t = Try {
      list()
      `match`(Lexer.EOF_TYPE)
    }
    release()
    t.isSuccess
  }

  def speculate_stat_alt2(): Boolean = {
    println("attempt alternative 2")
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
    `match`(MemoizeLexer.EQUALS)
    list()
  }

  //list : '[' elements ']' ;
  def list(): Unit = {
    val startTokenIndex = index()
    if (isSpeculating() && alreadyParsedRule(list_memo)) return
    val t = Try(_list())
    if (isSpeculating()) memoize(list_memo, startTokenIndex, t.isSuccess)
  }

  def _list(): Unit = {
    println(s"parse list rule at token index: ${index()}")
    `match`(MemoizeLexer.LBRACK)
    elements()
    `match`(MemoizeLexer.RBRACK)
  }

  //  elements : element (',' element)*;
  def elements(): Unit = {
    element()
    while (LA(1) == MemoizeLexer.COMMA) {
      `match`(MemoizeLexer.COMMA)
      element()
    }
  }

  //  element : NAME '=' NAME | NAME | list;
  def element(): Unit = {
    if (LA(1) == MemoizeLexer.NAME && LA(2) == MemoizeLexer.EQUALS) {
      `match`(MemoizeLexer.NAME)
      `match`(MemoizeLexer.EQUALS)
      `match`(MemoizeLexer.NAME)
    }
    else if (LA(1) == MemoizeLexer.NAME) `match`(MemoizeLexer.NAME)
    else if (LA(1) == MemoizeLexer.LBRACK) list()
    else throw new IllegalStateException(s"expecting name or list; found ${LT(1)}")
  }
}
