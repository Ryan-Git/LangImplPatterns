package parsing.lexer

import parsing.lexer.Lexer._

/**
  * Created by hongdi.ren.
  */
abstract class Lexer(input: String) {

  var p: Int = 0
  var c: Char = input.charAt(p)

  def consume(): Unit = {
    p = p + 1
    if (p >= input.length) c = EOF
    else c = input.charAt(p)
  }

  def `match`(x: Char): Unit = {
    if (c == x) consume()
    else throw new IllegalArgumentException(s"expecting $x; found $c")
  }

  def nextToken(): Token

  def getTokenName(tokenType: Int): String
}

object Lexer {
  val EOF: Char = (-1).toChar
  val EOF_TYPE: Int = 1
}