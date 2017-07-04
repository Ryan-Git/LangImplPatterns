package parsing.ir.normalized

/**
  * Created by hongdi.ren.
  */
sealed trait EvalType {
  override def toString: String = this.getClass.getSimpleName.dropRight(1)
}

object Invalid extends EvalType

object IntegerType extends EvalType

object StringType extends EvalType

object Vector extends EvalType