package parsing.ir.normalized

import parsing.ir.homo.Token

/**
  * Created by hongdi.ren.
  */
case class AddNode(left: ExprNode, plus: Token, right: ExprNode) extends ExprNode(plus) {

  addChild(left)
  addChild(right)

  override val evalType: EvalType = IntegerType

}
