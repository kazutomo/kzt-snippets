package foobar

//import chisel3._
//import chisel3.util._
import chisel3.iotesters._

class DynamicBusUnitTester(c: DynamicBus) extends PeekPokeTester(c) {

  val amax = c.amax
  val bmax = c.bmax
  val nbits = c.nbits

  val seed = 123
  var r = new scala.util.Random(seed)
  val maxval = 10

  for (n <- 1 to amax) {
    val a = List.tabulate(amax)(dummy => r.nextInt(maxval)+101)
    val b = List.tabulate(bmax)(dummy => r.nextInt(maxval)+1)

    poke(c.io.alen, n)
    for (i <- 0 until amax) poke(c.io.a(i), a(i))
    for (i <- 0 until bmax) poke(c.io.b(i), b(i))

    print("IN : ")
    a.foreach(v => print(f"$v "))
    b.foreach(v => print(f"$v "))
    println()

    print("OUT: ")
    for (i <- 0 until n+bmax) {
      if (i<n) {
        expect(c.io.out(i), a(i))
      } else {
        expect(c.io.out(i), b(i-n))
      }
      val tmp = peek(c.io.out(i))
      print(f"$tmp ")
    }
    println()
  }
}
