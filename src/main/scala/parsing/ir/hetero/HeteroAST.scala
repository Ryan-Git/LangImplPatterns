package parsing.ir.hetero

import parsing.ir.homo.Token

/**
  * Created by hongdi.ren.
  */
abstract class HeteroAST(token: Token) {

  def this() = this(null)

  def getNodeType: Int = token.`type`

  def isNil: Boolean = token == null

  override def toString: String = if (isNil) "nil" else token.toString
}
