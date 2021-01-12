package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}
import testutil._

class ConcatZeroStripUnitTester(c: ConcatZeroStrip) extends PeekPokeTester(c) {
  val nelems_in = c.nelems
  val nelems_out = nelems_in * 2 // for convenience
  val bw = c.bw
  val maxval = (BigInt(1) << bw) - BigInt(1)

  for (i <- 0 until nelems_in) {
    poke(c.io.inA(i), maxval)
    poke(c.io.inB(i), maxval)
  }
  poke(c.io.inAmask, (1<<nelems_in)-1)
  poke(c.io.inBmask, (1<<nelems_in)-1)

  val outmask = peek(c.io.outmask).toLong
  println("mask: " + TestUtil.convLongToBinStr(outmask, nelems_out))
  for (i <- 0 until nelems_out )
    printf("%016x ", peek(c.io.out(i)))
  println()
}

object ConcatZeroStripTest {

  def run(args: Array[String]) {
    // TODO: write a helper function for options
    val (args2, nelems_in) = TestUtil.getoptint(args, "nelem", 2)
    val (args3, bw) = TestUtil.getoptint(args2, "bw", 64)

    val dut = () => new ConcatZeroStrip(nelems_in, bw)
    val tester = c => new ConcatZeroStripUnitTester(c)
    TestUtil.driverhelper(args3, dut, tester)
  }
}
