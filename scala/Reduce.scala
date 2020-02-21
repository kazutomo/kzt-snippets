import scala.math._


object Reduce extends App {
  def sum(s: Seq[Int]): Int = {s.sum}

  val l = List(3,5,10,2,9) // List is a Seq

  println("sum=" + sum(l))

  val lred = l.reduce(_ + _)
  println("lred=" + lred)


  val l2 = List(1,2,0,8)

  val lred2 = l2.reduce(_ | _)
  println("lred2=" + lred2.toBinaryString)
}
