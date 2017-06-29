package parsing.ir.hetero

import parsing.ir.homo.Token
import parsing.ir.normalized.EvalType

/**
  * Created by hongdi.ren.
  */
abstract class ExprNode(token: Token) extends HeteroAST(token) {

  val evalType: EvalType

  def toStringTree: String = super.toString + s"<type=$evalType>"
}