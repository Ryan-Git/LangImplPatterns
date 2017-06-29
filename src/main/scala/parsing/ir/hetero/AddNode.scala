package parsing.ir.hetero

import parsing.ir.homo.Token
import parsing.ir.normalized.{EvalType, IntegerType}

/**
  * Created by hongdi.ren.
  */
case class AddNode(left: ExprNode, add: Token, right: ExprNode) extends ExprNode(add) {
  override val evalType: EvalType = IntegerType

  override def toStringTree: String = {
    if (left == null || right == null) this.toString
    else s"(${super.toStringTree} ${left.toStringTree} ${right.toStringTree})"
  }
}
