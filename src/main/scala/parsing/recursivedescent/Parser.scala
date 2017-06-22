package parsing.recursivedescent

import parsing.lexer.{Lexer, Token}

/**
  * Created by hongdi.ren.
  */
abstract class Parser(input: Lexer) {

  var lookahead: Token = input.nextToken()

  def `match`(x: Int): Unit = {
    if (lookahead.`type` == x) consume()
    else throw new IllegalStateException(s"expecting ${input.getTokenName(x)}; found $lookahead")
  }

  def consume(): Unit = lookahead = input.nextToken()
}
