
object RepeatedParams extends App {

  def foo(s: String*) = for (i <- s) println(i)

  def bar(s: String*) : List[String] = s.toList

  val str = "blue in green"
  val star = str.split(" ")

  foo("kind", "of", "blue")

  foo(star: _*)  // to convert it to repeated args

  println( bar(start: _*) )
}
