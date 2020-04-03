
object MethodSel extends App {

  class Dummy {
    def one(s: String) : Unit = println("one:" + s)

    def two(s: String) : Unit = println("two:" + s)
  }

  def pr(op: Dummy => Unit) : Unit = {
    val d = new Dummy()
    op(d)  // the caller chooses the method
  }

  pr { d => d.one("zzz") }

  pr { d => d.two("zzz") }
}
