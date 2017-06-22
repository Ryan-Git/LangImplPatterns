package parsing.memoize

import parsing.UnitSpec

/**
  * Created by hongdi.ren.
  */
class MemoizeParserTest extends UnitSpec {

  "MemoizeParser" should "exit silently for correct input" in {
    val lexer = new MemoizeLexer("[a,b]=[c,d]")
    val parser = new MemoizeParser(lexer)
    noException should be thrownBy parser.stat()
  }
}
