
import java.util.Date

object Func {
  def main(args: Array[String]) {
    foo(bar())
    namedargs(y=12)
    varargs(1,2,5)
    println(highorder(v2s, 123) + "\n")

    // anonymous function
    var mul = (x: Double, y: Double) => x * y
    println(mul(3,2))
    var userDir = () => {System.getProperty("user.dir")}
    println(userDir)

    // partially
    val pfunc = afunc(new Date, _: String)
    pfunc("zzz")
    pfunc("yyy")
  }

  def afunc(d : Date , s: String) = {
    println(s + " => " + d)
  }

  def foo( b: => Double) = {
    println("In foo()")
    println("bar()=" + b) // bar() is evaluated here
    println()
  }

  def bar() = {
    println("In bar()")
    System.nanoTime
  }

  def namedargs(x:Int = 10, y:Int) = {
    println("x=" + x + " y=" + y)
    println()
  }

  def varargs( vs:Int* ) = {
    println("len=" + vs.length)
    for (v <- vs) {
      println(v)
    }
    println()
  }

  def highorder(f: Int => String, v: Int ) = f(v)

  def v2s[T](x: T) = "<<" + x.toString() + ">>"
}
