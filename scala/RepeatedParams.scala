
object RepeatedParams extends App {

  def foo(s: String*) = for (i <- s) println(i)

  val str = "blue in green"
  val star = str.split(" ")

  foo("kind", "of", "blue")

  foo(star: _*)  // to convert it to repeated args
}
