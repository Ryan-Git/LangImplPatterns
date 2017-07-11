package parsing.combinator.parser

import parsing.combinator._
import parsing.combinator.lexer._

import scala.util.parsing.combinator.Parsers
import scala.util.parsing.input.{NoPosition, Position, Reader}

/**
  * Created by hongdi.ren.
  */
object WorkflowParser extends Parsers {

  override type Elem = WorkflowToken

  def apply(tokens: Seq[WorkflowToken]): Either[WorkflowParserError, WorkflowAST] = {
    val reader = new WorkflowTokenReader(tokens)
    program(reader) match {
      case NoSuccess(msg, next) => Left(WorkflowParserError(Location(next.pos.line, next.pos.column), msg))
      case Success(result, next) => Right(result)
    }
  }

  private def identifier: Parser[IDENTIFIER] = positioned {
    accept("identifier", { case id@IDENTIFIER(_) => id })
  }

  private def literal: Parser[LITERAL] = positioned {
    accept("string literal", { case lit@LITERAL(_) => lit })
  }

  private def condition: Parser[Equals] = positioned {
    (identifier ~ EQUALS() ~ literal) ^^ { case IDENTIFIER(id) ~ _ ~ LITERAL(lit) => Equals(id, lit) }
  }

  private def program: Parser[WorkflowAST] = positioned {
    phrase(block)
  }

  private def block: Parser[WorkflowAST] = positioned {
    rep1(statement) ^^ (stmtList => stmtList reduceRight AndThen)
  }

  private def statement: Parser[WorkflowAST] = positioned {
    val exit = EXIT() ^^ (_ => Exit())
    val readInput = READINPUT() ~ rep(identifier ~ COMMA()) ~ identifier ^^ {
      case _ ~ inputs ~ IDENTIFIER(lastInput) => ReadInput(inputs.map(_._1.str) ++ List(lastInput))
    }
    val callService = CALLSERVICE() ~ literal ^^ {
      case _ ~ LITERAL(serviceName) => CallService(serviceName)
    }
    val switch = SWITCH() ~ COLON() ~ INDENT() ~ rep1(ifThen) ~ opt(otherwiseThen) ~ DEDENT() ^^ {
      case _ ~ _ ~ _ ~ ifs ~ otherwise ~ _ => Choice(ifs ++ otherwise)
    }
    exit | readInput | callService | switch
  }

  private def ifThen: Parser[IfThen] = positioned {
    (condition ~ ARROW() ~ INDENT() ~ block ~ DEDENT()) ^^ {
      case cond ~ _ ~ _ ~ block ~ _ => IfThen(cond, block)
    }
  }

  private def otherwiseThen: Parser[OtherwiseThen] = positioned {
    (OTHERWISE() ~ ARROW() ~ INDENT() ~ block ~ DEDENT()) ^^ {
      case _ ~ _ ~ _ ~ block ~ _ => OtherwiseThen(block)
    }
  }
}

class WorkflowTokenReader(tokens: Seq[WorkflowToken]) extends Reader[WorkflowToken] {
  override def first: WorkflowToken = tokens.head

  override def atEnd: Boolean = tokens.isEmpty

  override def pos: Position = NoPosition

  override def rest: Reader[WorkflowToken] = new WorkflowTokenReader(tokens.tail)
}