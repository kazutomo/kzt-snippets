
import scala.math._

object Hello extends App {
  println("n_args=" + args.length)

  // args.foreach(println(_))

  for (a <- args if a.startsWith("-")) println(a)

  printf("Hello %s\n", "World")

  // val modifier is used to define constants or immutable
  val collection = List(1,2,3,4,5)
  println( "collection:" + collection.map( a => a*a ) )

  val f1 = (s1:String, s2:String) => s1 + s2
  // var modifier is used to define variables or mutable
  var f2 = (_:String) + (_:String)
  println( f1("aa", "11"), f2("bb", "22")  )
  f2 = (_:String) + (_:String) + "aaa"
  println( f2("bb", "22")  )


  var ar = new Array[String](5)
  ar(0) = "zero"; ar(1) = "one"
  println("len:" + ar.length)

  // lazy val. evaluate later
  lazy val lv = { println("lv initialized"); 100}
  println("before accessing lv")
  println(lv+10)

  val l = List(3,5,10,2,9)
  // val lred = l.reduce((x, y) => x max y)
  val lred = l.reduce(_ + _)
  println( "lred=" + lred)

  val testpats = Array("0001", "1100", "0000", "1111")
  for (t <- testpats) {
    val	x = Integer.parseInt(t, 2)
    println(t)
    if ( (x & 8) != 0 ) print("1 ") else print("0 ")
    if ( (x & 4) != 0 ) print("1 ") else print("0 ")
    if ( (x & 2) != 0 ) print("1 ") else print("0 ")
    if ( (x & 1) != 0 ) print("1 ") else print("0 ")
    println("")
  }


  val mlines = """abs
|foo
|bar""".stripMargin

  println(mlines)

}
