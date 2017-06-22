package parsing.multi

/**
  * Created by hongdi.ren.
  */

abstract class Parser(input: Lexer, k: Int) {

  val lookahead: Array[Token] = Array.ofDim(k)

  protected var p: Int = 0

  (1 to k).foreach(_ => consume())

  def consume(): Unit = {
    lookahead(p) = input.nextToken()
    p = (p + 1) % k
  }

  def LT(i: Int): Token = lookahead((p + i - 1) % k)

  def LA(i: Int): Int = LT(i).`type`

  def `match`(x: Int): Unit = {
    if (LA(1) == x) consume()
    else throw new IllegalArgumentException(s"expecting ${input.getTokenName(x)}; found ${LT(1)}")
  }
}

class LookaheadParser(input: Lexer, k: Int) extends Parser(input, k) {

  //list : '[' elements ']' ;
  def list(): Unit = {
    `match`(LookaheadLexer.LBRACK)
    elements()
    `match`(LookaheadLexer.RBRACK)
  }

  //  elements : element (',' element)*;
  def elements(): Unit = {
    element()
    while (LA(1) == LookaheadLexer.COMMA) {
      `match`(LookaheadLexer.COMMA)
      element()
    }
  }

  //  element : NAME '=' NAME | NAME | list;
  def element(): Unit = {
    if (LA(1) == LookaheadLexer.NAME && LA(2) == LookaheadLexer.EQUALS) {
      `match`(LookaheadLexer.NAME)
      `match`(LookaheadLexer.EQUALS)
      `match`(LookaheadLexer.NAME)
    }
    else if (LA(1) == LookaheadLexer.NAME) `match`(LookaheadLexer.NAME)
    else if (LA(1) == LookaheadLexer.LBRACK) list()
    else throw new IllegalStateException(s"expecting name or list; found ${LT(1)}")
  }
}
