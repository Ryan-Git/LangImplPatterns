package parsing.ir.hetero

import parsing.ir.homo.Token
import parsing.ir.normalized.{EvalType, IntegerType}

/**
  * Created by hongdi.ren.
  */
case class IntNode(i: Token) extends ExprNode(i) {

  override val evalType: EvalType = IntegerType

}
