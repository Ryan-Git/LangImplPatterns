package parsing.ir

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

class IRParser(input: Lexer) extends Parser(input) {

  var root: ParseTree = _
  var current: ParseTree = _

  override def `match`(x: Int): Unit = {
    if (!isSpeculating()) current.addChild(TokenNode(LT(1)))
    super.`match`(x)
  }

  //stat: list EOF | assign EOF ;
  def stat(): Unit = {
    handleTree(RuleNode("stat"), {
      if (speculate_stat_alt1()) {
        list()
        `match`(Lexer.EOF_TYPE)
      } else if (speculate_stat_alt2()) {
        assign()
        `match`(Lexer.EOF_TYPE)
      } else throw new IllegalStateException(s"expecting stat found ${LT(1)}")
    })
  }

  def handleTree[T](r: ParseTree, body: => T): T = {
    if (!isSpeculating()) {
      if (root == null) root = r
      else current.addChild(r)
      val temp = current
      current = r
      val ret = body
      current = temp
      ret
    } else
      body
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
    handleTree(RuleNode("assign"), {
      list()
      `match`(IRLexer.EQUALS)
      list()
    })
  }

  //list : '[' elements ']' ;
  def list(): Unit = {
    handleTree(RuleNode("list"), {
      `match`(IRLexer.LBRACK)
      elements()
      `match`(IRLexer.RBRACK)
    })
  }

  //  elements : element (',' element)*;
  def elements(): Unit = {
    handleTree(RuleNode("elements"), {
      element()
      while (LA(1) == IRLexer.COMMA) {
        `match`(IRLexer.COMMA)
        element()
      }
    })
  }

  //  element : NAME '=' NAME | NAME | list;
  def element(): Unit = handleTree(RuleNode("element"), {
    if (LA(1) == IRLexer.NAME && LA(2) == IRLexer.EQUALS) {
      `match`(IRLexer.NAME)
      `match`(IRLexer.EQUALS)
      `match`(IRLexer.NAME)
    }
    else if (LA(1) == IRLexer.NAME) `match`(IRLexer.NAME)
    else if (LA(1) == IRLexer.LBRACK) list()
    else throw new IllegalStateException(s"expecting name or list; found ${LT(1)}")
  }
  )
}
