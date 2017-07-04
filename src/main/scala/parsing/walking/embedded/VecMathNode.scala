package parsing.walking.embedded

/**
  * Created by hongdi.ren.
  */
abstract class VecMathNode(token: Token) extends HeteroAST(token) {

  def print(): Unit = System.out.print(if (token != null) token.toString else "<null>")
}
