package parsing.ir.hetero

import parsing.ir.homo.Token

/**
  * Created by hongdi.ren.
  */
object HeteroASTTest extends App {

  val plus = Token(Token.PLUS, "+")
  val one = Token(Token.INT, "1")
  val two = Token(Token.INT, "2")

  val root = AddNode(IntNode(one), plus, IntNode(two))
  println(root.toStringTree)
}
