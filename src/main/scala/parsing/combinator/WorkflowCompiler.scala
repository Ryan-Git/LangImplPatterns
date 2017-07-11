package parsing.combinator

import parsing.combinator.lexer.WorkflowLexer
import parsing.combinator.parser.{WorkflowAST, WorkflowParser}

/**
  * Created by hongdi.ren.
  */
object WorkflowCompiler {
  def apply(code: String): Either[WorkflowCompilationError, WorkflowAST] = {
    for {
      tokens <- WorkflowLexer(code).right
      ast <- WorkflowParser(tokens).right
    } yield ast
  }
}
