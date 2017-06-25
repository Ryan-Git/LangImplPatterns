package parsing.ir

import scala.collection.mutable

/**
  * Created by hongdi.ren.
  */
trait ParseTree {
  val children: mutable.ArrayBuffer[ParseTree] = mutable.ArrayBuffer[ParseTree]()

  def addChild(v: ParseTree): ParseTree = {
    children += v
    this
  }

  def addChild(v: Seq[ParseTree]): ParseTree = {
    children ++= v
    this
  }

  def treeString(lvl: Int = 0): String = {
    val sb = new mutable.StringBuilder(1000)
    sb.append(this.toString).append("\n")
    children.foreach(t => sb.append("\t".*(lvl)).append("-: ").append(t.treeString(lvl + 1)))
    sb.toString()
  }
}

case class RuleNode(value: String) extends ParseTree

object RuleNode {
  def apply(value: String, children: Seq[ParseTree]): ParseTree = new RuleNode(value).addChild(children)
}

case class TokenNode(token: Token) extends ParseTree

object TokenNode {
  def apply(value: Token, children: Seq[ParseTree]): ParseTree = new TokenNode(value).addChild(children)
}