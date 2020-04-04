
object PatternMatch extends App {

  def sizeof(x: Any) = x match {
    case b: Byte => 1
    case s: Short => 2
    case i: Int => 4
    case l: Long => 8
    case s: Float => 4
    case d: Double => 8
    case _ => 0
  }

  val l = List[Any](1.toByte, 2.toShort, 3, 4.toLong, 5.5.toFloat, 6.6)

  l foreach{ v => println( sizeof(v) ) }
}
