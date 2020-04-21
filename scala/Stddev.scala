object Stddev extends App {

  // non-generic implementation, but this may be ok for most of cases
  def stddev(x: List[Double]) : Double = {
    val m = x.sum / x.size
    val sq = x map(v => (v-m)*(v-m))
    math.sqrt( sq.sum / x.size )
  }

  val x = List[Double](20,30,2,23,10,55,234,1)

  println(stddev(x))

}
