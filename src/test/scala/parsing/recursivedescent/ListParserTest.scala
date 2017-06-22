package parsing.recursivedescent

import parsing.UnitSpec
import parsing.lexer.ListLexer

/**
  * Created by hongdi.ren.
  */
class ListParserTest extends UnitSpec {

  "ListParser" should "throw exception for incorrect input" in {
    val lexer = new ListLexer("[a, ]")
    val parser = new ListParser(lexer)
    val e = the[IllegalStateException] thrownBy parser.list()
    e.getMessage should be("expecting name or list; found <']',RBRACK>")
  }

  it should "exit silently for correct input" in {
    val lexer = new ListLexer("[a, b ]")
    val parser = new ListParser(lexer)
    noException should be thrownBy parser.list()
  }
}
