package parsing.combinator.lexer

import parsing.combinator._

import scala.util.matching.Regex
import scala.util.parsing.combinator.RegexParsers

/**
  * Created by hongdi.ren.
  */
object WorkflowLexer extends RegexParsers {
  override def skipWhitespace: Boolean = true

  override protected val whiteSpace: Regex = "[ \t\r\f]+".r

  def apply(code: String): Either[WorkflowLexerError, List[WorkflowToken]] = {
    parse(tokens, code) match {
      case NoSuccess(msg, next) => Left(WorkflowLexerError(Location(next.pos.line, next.pos.column), msg))
      case Success(result, next) => Right(result)
    }
  }

  private def tokens: Parser[List[WorkflowToken]] = {
    phrase(rep1(exit | readInput | callService | switch | otherwise | colon | arrow
      | equals | comma | literal | identifier | indentation)) ^^ { rawTokens =>
      processIndentations(rawTokens)
    }
  }

  private def processIndentations(tokens: List[WorkflowToken],
                                  indents: List[Int] = List(0)): List[WorkflowToken] = {
    tokens.headOption match {

      // if there is an increase in indentation level, we push this new level into the stack
      // and produce an INDENT
      case Some(INDENTATION(spaces)) if spaces > indents.head =>
        INDENT() :: processIndentations(tokens.tail, spaces :: indents)

      // if there is a decrease, we pop from the stack until we have matched the new level,
      // producing a DEDENT for each pop
      case Some(INDENTATION(spaces)) if spaces < indents.head =>
        val (dropped, kept) = indents.partition(_ > spaces)
        (dropped map (_ => DEDENT())) ::: processIndentations(tokens.tail, kept)

      // if the indentation level stays unchanged, no tokens are produced
      case Some(INDENTATION(spaces)) if spaces == indents.head =>
        processIndentations(tokens.tail, indents)

      // other tokens are ignored
      case Some(token) =>
        token :: processIndentations(tokens.tail, indents)

      // the final step is to produce a DEDENT for each indentation level still remaining, thus
      // "closing" the remaining open INDENTS
      case None =>
        indents.filter(_ > 0).map(_ => DEDENT())

    }
  }

  private def identifier: Parser[IDENTIFIER] = positioned {
    "[a-zA-Z_][a-zA-Z0-9_]*".r ^^ { str => IDENTIFIER(str) }
  }

  private def literal: Parser[LITERAL] = positioned {
    """"[^"]*"""".r ^^ { str =>
      val content = str.substring(1, str.length - 1)
      LITERAL(content)
    }
  }

  private def indentation: Parser[INDENTATION] = positioned {
    "\n[ ]*".r ^^ { whitespace =>
      val nSpaces = whitespace.length - 1
      INDENTATION(nSpaces)
    }
  }

  private def exit: Parser[EXIT] = positioned {
    "exit" ^^ (_ => EXIT())
  }

  private def readInput: Parser[READINPUT] = positioned {
    "read input" ^^ (_ => READINPUT())
  }

  private def callService: Parser[CALLSERVICE] = positioned {
    "call service" ^^ (_ => CALLSERVICE())
  }

  private def switch: Parser[SWITCH] = positioned {
    "switch" ^^ (_ => SWITCH())
  }

  private def otherwise: Parser[OTHERWISE] = positioned {
    "otherwise" ^^ (_ => OTHERWISE())
  }

  private def colon: Parser[COLON] = positioned {
    ":" ^^ (_ => COLON())
  }

  private def arrow: Parser[ARROW] = positioned {
    "->" ^^ (_ => ARROW())
  }

  private def equals: Parser[EQUALS] = positioned {
    "==" ^^ (_ => EQUALS())
  }

  private def comma: Parser[COMMA] = positioned {
    "," ^^ (_ => COMMA())
  }

}
