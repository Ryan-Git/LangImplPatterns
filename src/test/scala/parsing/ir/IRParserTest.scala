package parsing.ir

import parsing.UnitSpec

/**
  * Created by hongdi.ren.
  */
class IRParserTest extends UnitSpec {


  "IRParser" should "give back correct parse tree" in {
    val lexer = new IRLexer("[a,b]=[c,d]")
    val parser = new IRParser(lexer)
    noException should be thrownBy parser.stat()
    val root = parser.root
    println(root.treeString())
  }
}
