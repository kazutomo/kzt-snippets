
object Rand extends App {

  val r1 = scala.util.Random
  val limit = 8
  println(r1.nextInt(limit))

  val seed = 100
  val r2 = new scala.util.Random(seed)  // <= require 'new'

  println(r2.nextDouble)  // does not take limit, it generates uniformly from 0.0 (inclusive) to 1.0 (exclusive)

  val s = Seq.fill(10)(r2.nextInt(limit))
  println(s)


  val tmp = r2.nextInt(limit)
  println(tmp)
  val lpat = Seq.tabulate(8)( x => tmp & (1 << x) )
  println(lpat)
}
