package parsing.ir.normalized

import parsing.ir.homo.Token

/**
  * Created by hongdi.ren.
  */
case class IntNode(i: Token) extends ExprNode(i) {

  override val evalType: EvalType = Integer

}
