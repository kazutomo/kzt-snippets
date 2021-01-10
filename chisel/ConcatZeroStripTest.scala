package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}
import testutil._

class ConcatZeroStripUnitTester(c: ConcatZeroStrip) extends PeekPokeTester(c) {
  val nelems_in = c.nelems

  // binarized int (to print binary with %d)
  def BI(a: Int) : Int = a.toBinaryString.toInt

  poke(c.io.inA(0), 11)
  poke(c.io.inA(1), 22)
  poke(c.io.inB(0), 33)
  poke(c.io.inB(1), 44)
  poke(c.io.inAmask, 0)
  poke(c.io.inBmask, 3)

  val outmask = peek(c.io.outmask).toInt
  printf("mask=%04d: ", BI(outmask))
  for (i <- 0 until nelems_in*2 )
    printf("%d ", peek(c.io.out(i)).toInt)
  printf("\n")
}

object ConcatZeroStripTest {

  def run(args: Array[String]) {
    val (args2, nelems_in) = TestUtil.getoptint(args, "nelem", 40)
    val (args3, bw) = TestUtil.getoptint(args, "bw", 64)

    val dut = () => new ConcatZeroStrip(nelems_in, bw)
    val tester = c => new ConcatZeroStripUnitTester(c)
    TestUtil.driverhelper(args3, dut, tester)
  }
}