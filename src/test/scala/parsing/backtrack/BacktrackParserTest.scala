package parsing.backtrack

import parsing.UnitSpec

/**
  * Created by hongdi.ren.
  */
class BacktrackParserTest extends UnitSpec {

  "BacktrackParser" should "throw exception for incorrect input" in {
    val lexer = new BacktrackLexer("[a,b]=[c,d,,]")
    val parser = new BacktrackParser(lexer)
    val e = the[Exception] thrownBy parser.stat()
    e.getMessage should be("expecting stat found <'[',LBRACK>")
  }

  it should "exit silently for correct input" in {
    val lexer = new BacktrackLexer("[a,b,c=d]=[c,d,e=f]")
    val parser = new BacktrackParser(lexer)
    noException should be thrownBy parser.stat()
  }
}
