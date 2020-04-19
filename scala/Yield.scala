
import java.io._
import Array._

object Yield extends App {

  def mmultiply(a: Array[Array[Int]], b: Array[Array[Int]]) : Array[Array[Int]] = {

    for (r <- a)
    yield {
      for (c <- b.transpose)
      yield {
        r zip c map Function.tupled(_ * _) reduceLeft(_ + _)
      }
    }
  }

  val a = Array(Array(1,2), Array(3,4))
  val b = Array(Array(0,1), Array(1,0))

  val c = mmultiply(a, b)

  for (r <- c) {
    r foreach (e => print(e + " "))
    println()
  }
}
