package parsing.lexer

/**
  * Created by hongdi.ren.
  */
case class Token(`type`: Int, text: String) {

  override def toString: String = s"""<'$text',${ListLexer.tokenNames(`type`)}>"""
}
