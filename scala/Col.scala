
object Col {
  def main(args: Array[String]) {

    val nn = 1 :: 2 :: 3 :: Nil
    val mm = 88 :: (20 :: (99 :: Nil))
    val nnmm = nn ::: mm

    println("head=" + nn.head)
    println("tail=" + nn.tail)
    println("rev=" + nn.reverse)
    println("empty?=" + nn.isEmpty)

    println("len=" + nnmm.length)

    val zeros = List.fill(10)(0)
    println(zeros)

    println(List.tabulate(2, 3)(_ * _))

    // Set
    val s1 = Set(2,5,7)
    val s2 = Set(2,5,9)

    println( s1.&(s2) )
    println( s1.intersect(s2) )

    // Map
    var m: Map[String,Int] = Map()

    m += ("one" -> 1)
    m += ("two" -> 2)
    println(m)
    println(m.keys)
    println(m.values)
    println(m("one"))

    m.keys.foreach{ k =>
      print( "k=" + k )
      println(" v=" + m(k)) }

    if( m.contains("one") ) {
      println("one is included")
    }

    // Tuples
    val t = (1, "s", Console)

    println(t)

    // Iterator
    val it = Iterator("a", "b", "c")
    while (it.hasNext) {
      println(it.next())
    }
  }
}
