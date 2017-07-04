package parsing.walking.embedded

import parsing.ir.normalized.EvalType

/**
  * Created by hongdi.ren.
  */
abstract class ExprNode(token: Token) extends VecMathNode(token) {

  val evalType: EvalType

  def toStringTree: String = super.toString + s"<type=$evalType>"
}