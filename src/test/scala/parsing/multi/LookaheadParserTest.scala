package parsing.multi

import parsing.UnitSpec

/**
  * Created by hongdi.ren.
  */
class LookaheadParserTest extends UnitSpec {

  "LookaheadParser" should "throw exception for incorrect input" in {
    val lexer = new LookaheadLexer("[a, b=c,, [d,e] ]")
    val parser = new LookaheadParser(lexer, 2)
    val e = the[IllegalStateException] thrownBy parser.list()
    e.getMessage should be("expecting name or list; found <',',COMMA>")
  }

  it should "exit silently for correct input" in {
    val lexer = new LookaheadLexer("[a, b=c, [d,e] ]")
    val parser = new LookaheadParser(lexer, 2)
    noException should be thrownBy parser.list()
  }
}
