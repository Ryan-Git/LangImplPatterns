package parsing.backtrack

import parsing.multi.Lexer._
import parsing.multi.LookaheadLexer._

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

class BacktrackLexer(input: String) extends Lexer(input) {

  override def getTokenName(x: Int): String = tokenNames(x)

  override def nextToken(): Token = {
    while (c != EOF) {
      c match {
        case _ if blank(c) => WS()

        case ',' =>
          consume()
          return Token(COMMA, ",")

        case '[' =>
          consume()
          return Token(LBRACK, "[")

        case ']' =>
          consume()
          return Token(RBRACK, "]")

        case '=' =>
          consume()
          return Token(EQUALS, "=")

        case _ if isLETTER() => return NAME()

        case _ => throw new IllegalArgumentException("invalid character: " + c)
      }
    }

    Token(EOF_TYPE, "<EOF>")
  }

  def isLETTER(): Boolean = c.isLetter

  def NAME(): Token = {
    val sb = new StringBuilder
    do {
      sb.append(c)
      consume()
    } while (isLETTER())

    Token(BacktrackLexer.NAME, sb.toString())
  }

  def WS(): Unit = while (blank(c)) consume()
}

object BacktrackLexer {
  val NAME: Int = 2
  val COMMA: Int = 3
  val LBRACK: Int = 4
  val RBRACK: Int = 5
  val EQUALS: Int = 6

  val tokenNames: IndexedSeq[String] = Vector("n/a", "<EOF>", "NAME", "COMMA", "LBRACK", "RBRACK", "EQUALS")

  val blank: Set[Char] = Set(' ', '\t', '\n', '\r')
}
