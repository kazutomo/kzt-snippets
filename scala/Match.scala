object Match {
  def foo(a: Int) : String = a.toString

  def main(args: Array[String]) {

    val mode = 0

    val m = mode match {
      case 1 => "one"
      case 2 => "two"
      case _ => {"etc" + foo(mode)}
    }

    println(m)
  }
}
