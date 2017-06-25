package parsing.ir.homo

/**
  * Created by hongdi.ren.
  */
case class Token(`type`: Int, text: String) {

  override def toString: String = text
}

object Token {
  val PLUS: Int = 1
  val INT: Int = 2
}