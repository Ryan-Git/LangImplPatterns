package parsing.combinator

import parsing.combinator.lexer.WorkflowLexer

/**
  * Created by hongdi.ren.
  */
object WorkflowTest extends App {

  private val code =
    """read input name, country
      |switch:
      |  country == "PT" ->
      |    call service "A"
      |    exit
      |  otherwise ->
      |    call service "B"
      |    switch:
      |      name == "unknown" ->
      |        exit
      |      otherwise ->
      |        call service "C"
      |        exit""".stripMargin

  println(WorkflowLexer(code).right)
  (1 to 3).foreach(_ => println())
  println(WorkflowCompiler(code).right)
}
