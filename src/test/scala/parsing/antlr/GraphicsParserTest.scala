package parsing.antlr

import org.antlr.v4.runtime._
import parsing.UnitSpec

/**
  * Created by hongdi.ren.
  */
class GraphicsParserTest extends UnitSpec {

  "GraphicsParser" should "throw exception for incorrect input" in {
    val lexer = new GraphicsLexer(CharStreams.fromString("line to 2,3"))
    val tokens = new CommonTokenStream(lexer)
    val parser = new GraphicsParser(tokens)
    val listener = new MyErrorListener()
    parser.addErrorListener(listener)
    parser.file()
    val exceptionList = listener.getExceptionList
    exceptionList.size should be > 0
  }

  "GraphicsParser" should "exit silently for correct input" in {
    val lexer = new GraphicsLexer(CharStreams.fromString("line from 10,0 to 0,0"))
    val tokens = new CommonTokenStream(lexer)
    val parser = new GraphicsParser(tokens)
    val listener = new MyErrorListener()
    parser.addErrorListener(listener)
    parser.file()
    val exceptionList = listener.getExceptionList
    exceptionList.size should be(0)
  }
}