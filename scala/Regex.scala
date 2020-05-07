object Regex extends App {
  val rstr = "([0-9]+) +([0-9]+)".r
  val tests = List("12  333", "foo 112")

  for(t <- tests) {
    val a = t match {
      case rstr(first, second) => (first, second)
      case _ => ("", "")
    }
    println(a)
  }
}
