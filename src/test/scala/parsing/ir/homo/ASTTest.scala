package parsing.ir.homo

import parsing.UnitSpec

/**
  * Created by hongdi.ren.
  */
class ASTTest extends UnitSpec {

  "AST" should "construct proper tree" in {
    val plus = Token(Token.PLUS, "+")
    val one = Token(Token.INT, "1")
    val two = Token(Token.INT, "2")

    val root = new AST(plus)
    root.addChild(new AST(one))
    root.addChild(new AST(two))
    println(root.treeString())

    val list = new AST()
    list.addChild(new AST(one))
    list.addChild(new AST(two))
    println(list.treeString())
  }
}
