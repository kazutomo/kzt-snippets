import scala.util.control._

object Loop extends App {

  for (a <- args) println(a)



  val l1 = List(1,2,4)
  val l2 = List(2,3,4)

  val r = for{x <- l1 if x != 2 } yield x
  for (x <- r)  println(x)
  println()

  val outer = new Breaks;
  val inner = new Breaks;

  outer.breakable {
    for (a <- l1) {
      inner.breakable {
        for (b <- l2) {
          println("a="+a+" b="+b)
          if (a+b == 3) {
            inner.break;
          }
        }
      }
    }
  }
  println()
}
