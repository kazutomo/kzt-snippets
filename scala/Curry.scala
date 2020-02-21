
object Curry
{
  def  func(a: Int)(b: Int) = a * b

  def main(args: Array[String])
  {
    val m5 = func(5)_

    println(m5(10))
  }
}
