package parsing.ir.homo

import parsing.ir.IRLexer

import scala.collection.mutable

/**
  * Created by hongdi.ren.
  */
class AST(token: Token) {

  def this() = this(null)

  def this(tokenType: Int) = this(Token(tokenType, IRLexer.tokenNames(tokenType)))

  val children: mutable.ArrayBuffer[AST] = mutable.ArrayBuffer()

  def getNodeType: Int = token.`type`

  def addChild(t: AST): Unit = children += t

  def isNil: Boolean = token == null

  override def toString: String = if (isNil) "nil" else token.toString

  def treeString(lvl: Int = 0): String = {
    val sb = new mutable.StringBuilder(1000)
    if (isNil)
      sb.append("[]")
    else
      sb.append(token.toString)
    sb.append("\n")
    children.foreach(t => sb.append("\t".*(lvl)).append("-: ").append(t.treeString(lvl + 1)))
    sb.toString()
  }
}
