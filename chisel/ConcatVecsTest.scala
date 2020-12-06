package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}

class ConcatVecsUnitTester(c: ConcatVecs) extends PeekPokeTester(c) {

  val alenmax = c.alenmax
  val blen = c.blen
  val nbits = c.nbits

  val seed = 123
  var r = new scala.util.Random(seed)
  val maxval = 10

  for (n <- 1 to alenmax) {
    val a = List.tabulate(alenmax)(dummy => r.nextInt(maxval)+101)
    val b = List.tabulate(blen)(dummy => r.nextInt(maxval)+1)

    poke(c.io.alen, n)
    for (i <- 0 until alenmax) poke(c.io.a(i), a(i))
    for (i <- 0 until blen) poke(c.io.b(i), b(i))

    print("IN : ")
    a.foreach(v => print(f"$v "))
    b.foreach(v => print(f"$v "))
    println()

    print("OUT: ")
    for (i <- 0 until n+blen) {
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

object ConcatVecsTest {
  val dut = () => new ConcatVecs()
  val tester = c => new ConcatVecsUnitTester(c)

  def run(args: Array[String], verilogonly: Boolean)  {
    if (verilogonly)
      chisel3.Driver.execute(args, dut)
    else
      iotesters.Driver.execute(args, dut) {tester}
  }
}
