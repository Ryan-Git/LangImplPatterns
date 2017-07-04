package parsing.walking.embedded

/**
  * Created by hongdi.ren.
  */
class AssignNode(id: VarNode, plus: Token, value: ExprNode) extends StatNode(plus) {
  override def print(): Unit = {
    id.print()
    System.out.print("=")
    value.print()
    System.out.println()
  }
}
