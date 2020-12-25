package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}

class NwayMuxUnitTester(c: NwayMux) extends PeekPokeTester(c) {
  val nelems = c.n
  val bw = c.bw

  def testnshift(n: Int)  {
    for (i <- 0 until nelems) poke(c.io.in(i), i)
    poke(c.io.nshift, n)

    for (i <- 0 until nelems) {
      if ( (i + n) < nelems) expect(c.io.out(i), i+n)
      else expect(c.io.out(i), 0)
    }
  }

  for (i <- 0 until nelems) testnshift(i)

}


object NwayMuxTest {
  val nelems = 80
  val bw = 64
  val dut = () => new NwayMux(nelems, bw)
  val tester = c => new NwayMuxUnitTester(c)

  def run() { TestMain.driverhelper(dut, tester) }
}
