package parsing.lexer

import parsing.lexer.Lexer._
import parsing.lexer.ListLexer._

/**
  * Created by hongdi.ren.
  */
class ListLexer(input: String) extends Lexer(input) {

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

    Token(ListLexer.NAME, sb.toString())
  }

  def WS(): Unit = while (blank(c)) consume()
}

object ListLexer {
  val NAME: Int = 2
  val COMMA: Int = 3
  val LBRACK: Int = 4
  val RBRACK: Int = 5

  val tokenNames: IndexedSeq[String] = Vector("n/a", "<EOF>", "NAME", "COMMA", "LBRACK", "RBRACK")

  val blank: Set[Char] = Set(' ', '\t', '\n', '\r')
}