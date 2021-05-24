package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}
import testutil._

class NwayMuxUnitTester(c: NwayMux) extends PeekPokeTester(c) {
  val nelems = c.n
  val bw = c.bw

  def testnshift(n: Int)  {
    for (i <- 0 until nelems) poke(c.io.in(i), i)
    poke(c.io.nshift, n)
    step(1)
    for (i <- 0 until nelems) {
      if ( (i + n) < nelems) expect(c.io.out(i), i+n)
      else expect(c.io.out(i), 0)
    }
  }

  for (i <- 0 until nelems) testnshift(i)

}


object NwayMuxTest {

  def run(args: Array[String]) {
    val nelems = 10
    val bw = 8
    val dut = () => new NwayMux(nelems, bw)
    val tester = c => new NwayMuxUnitTester(c)
    TestUtil.driverhelper(args, dut, tester)
  }
}
