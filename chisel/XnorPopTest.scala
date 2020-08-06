package foobar

// import chisel3._  // this causes errors.
import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}

class XnorPopUnitTester(c: XnorPop) extends PeekPokeTester(c) {

  val seed = 123
  val r = new scala.util.Random(seed)
  val ntests = 16

  val bitwidth =  c.bw
  val mask = (1 << bitwidth) - 1

  for (i <- 0 until ntests) {
    val a = r.nextInt & mask
    val b = r.nextInt & mask
    poke(c.io.in_a, a)
    poke(c.io.in_b, b)

    val output = peek(c.io.out).toInt // peek() returns bigInt

    val ref = (~(a^b)) & mask
    val reftmp = List.tabulate(8) { i => if ((ref & (1<<i))>0) 1 else 0 }
    val refcnt = reftmp.reduce {_ + _}

    expect(c.io.out, refcnt)

    printf("%4d => cnt=%4d (%4d)\n",  i, output, refcnt)
  }
}
