package parsing.walking.embedded

import parsing.ir.normalized.{EvalType, StringType}

/**
  * Created by hongdi.ren.
  */
case class VarNode(i: Token) extends ExprNode(i) {

  override val evalType: EvalType = StringType

  override def print(): Unit = System.out.print(i.toString)
}
