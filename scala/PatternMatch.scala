
object PatternMatch extends App {

  type AppOpts = Map[String, String]

  def usage() = println("Usage: PatternMatch [-h] [-x int] [-y int] filename")

  def nextopts(l: List[String], m: AppOpts) : AppOpts = {
    l match {
      case Nil => m
      case "-h" :: tail => usage() ; sys.exit(1)
      case "-x" :: istr :: tail => nextopts(tail, m ++ Map("x" -> istr ))
      case "-y" :: istr :: tail => nextopts(tail, m ++ Map("y" -> istr ))
      case str :: Nil => m ++ Map("filename" -> str)
      case unknown => {
        println("Unknown: " + unknown)
        sys.exit(1)
      }
    }
  }
  val opts = nextopts(args.toList, Map())
  println(opts)


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


  // Note: this only works for Array, not List due to the erasure rule
  def is2DIntArray(x: Any) = x match {
    case a: Array[Array[Int]] => "yes"
    case _ => "no"
  }

  val l2 = List[Any]( Array(1,2,3),
    Array[Array[Int]]( Array(1,2), Array(3,4)) )

  l2 foreach{ v => println( is2DIntArray(v) ) }
}
