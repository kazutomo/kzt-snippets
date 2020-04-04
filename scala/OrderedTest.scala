
object OrderedTest extends App {

  // note: 'case' implicitly adds a val prefix to 'n'. so 'n' is accessible
  case class Foo(n: Int) extends Ordered[Foo] {
    def compare(that: Foo) = { this.n - that.n }
  }

  val a = Foo(10)
  val b = Foo(2)

  println(a > b)
  println(b > a)
}
