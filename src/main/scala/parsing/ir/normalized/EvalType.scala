package parsing.ir.normalized

/**
  * Created by hongdi.ren.
  */
sealed trait EvalType {
  override def toString: String = this.getClass.getSimpleName.dropRight(1)
}

object Invalid extends EvalType

object Integer extends EvalType

object Vector extends EvalType