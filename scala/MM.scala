object MM extends App {

  def mmul(a: Array[Array[Int]], b: Array[Array[Int]]) : Array[Array[Int]] = {
    for(r <- a) yield {
      for(c <- b.transpose) yield r zip c map Function.tupled(_*_) reduceLeft (_+_)
    }
  }

  val a = Array(Array(3,2,1), Array(2,1,3), Array(1,0,3))

  val c = mmul(a,a)

  for (i <- 0 until c.length) {
    for (j <- 0 until c(0).length) {
      print(c(i)(j) + " ")
    }
    println()
  }
}
