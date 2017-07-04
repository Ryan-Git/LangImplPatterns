package parsing.walking.embedded

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

  override def print(): Unit = {
    left.print()
    System.out.print("+")
    right.print()
  }
}
