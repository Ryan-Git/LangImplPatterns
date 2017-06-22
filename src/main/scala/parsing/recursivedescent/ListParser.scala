package parsing.recursivedescent

import parsing.lexer.{Lexer, ListLexer}

/**
  * Created by hongdi.ren.
  */
class ListParser(input: Lexer) extends Parser(input) {

  //list : '[' elements ']' ;
  def list(): Unit = {
    `match`(ListLexer.LBRACK)
    elements()
    `match`(ListLexer.RBRACK)
  }

  //  elements : element (',' element)*;
  def elements(): Unit = {
    element()
    while (lookahead.`type` == ListLexer.COMMA){
      `match`(ListLexer.COMMA)
      element()
    }
  }

  //  element : NAME | list;
  def element(): Unit = {
    if (lookahead.`type` == ListLexer.NAME) `match`(ListLexer.NAME)
    else if (lookahead.`type` == ListLexer.LBRACK) list()
    else throw new IllegalStateException(s"expecting name or list; found $lookahead")
  }
}
