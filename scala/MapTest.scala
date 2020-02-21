import scala.math._


object MapTest extends App {
  val m = Map(
    "foo" -> 10,
    "bar" -> 20)

  val foo = if(m.contains("foo")) m("foo") else -1

  val zoo = if(m.contains("zoo")) m("zoo") else -1

  println(s"foo=$foo")
  println(s"zoo=$zoo")
}
