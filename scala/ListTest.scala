import scala.collection.mutable.ListBuffer

object ListTest extends App {
  var lb = new ListBuffer[List[Int]]()

  lb += List(1,2,3)
  lb += List(4,5)

  println(lb.flatten)
  val l = lb.flatten
  // println(lb.flatten(1))
  println(lb(1))
  println(l(1))

  println("len=" + lb.flatten.length)
  println("clear")
  lb.clear
  println("len=" + lb.length)
}
