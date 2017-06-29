package parsing.ir.normalized

import parsing.ir.homo.{AST, Token}

import scala.collection.mutable

/**
  * Created by hongdi.ren.
  */
abstract class ExprNode(token: Token) extends AST(token) {

  val evalType: EvalType

  override def treeString(lvl: Int = 0): String = {
    val t = evalType match {
      case Invalid => ""
      case _ => s"<type=$evalType>"
    }
    val sb = new mutable.StringBuilder(1000)
    if (isNil)
      sb.append("[]")
    else
      sb.append(token.toString).append(t)
    sb.append("\n")
    children.foreach(t => sb.append("\t".*(lvl)).append("-: ").append(t.treeString(lvl + 1)))
    sb.toString()
  }
}