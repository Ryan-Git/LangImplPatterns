package parsing.ir.normalized

import parsing.ir.homo.Token

/**
  * Created by hongdi.ren.
  */
case class VectorNode(v: Token, elements: Seq[ExprNode]) extends ExprNode(v) {

  elements.foreach(addChild)

  override val evalType: EvalType = Vector

}
