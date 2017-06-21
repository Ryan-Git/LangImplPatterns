package parsing.lexer

import org.scalatest.{FlatSpec, Matchers}
import parsing.lexer.Lexer._
import parsing.lexer.ListLexer._

/**
  * Created by hongdi.ren.
  */
class ListLexerSpec extends FlatSpec with Matchers {

  "ListLexer" should "nextToken" in {
    val input = "[a, b ]"
    val lexer = new ListLexer(input)

    lexer.nextToken() should be(Token(LBRACK, "["))
    lexer.nextToken() should be(Token(NAME, "a"))
    lexer.nextToken() should be(Token(COMMA, ","))
    lexer.nextToken() should be(Token(NAME, "b"))
    lexer.nextToken() should be(Token(RBRACK, "]"))
    lexer.nextToken() should be(Token(EOF_TYPE, "<EOF>"))
  }

}
