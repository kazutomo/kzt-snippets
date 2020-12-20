package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}

class NwayMuxUnitTester(c: NwayMux) extends PeekPokeTester(c) {

}


object NwayMuxTest {
  val nelems = 160
  val bw = 64
  val dut = () => new NwayMux(nelems, bw)
  val tester = c => new NwayMuxUnitTester(c)

  def run(args: Array[String], verilogonly: Boolean)  {
    if (verilogonly)
      chisel3.Driver.execute(args, dut)
    else
      iotesters.Driver.execute(args, dut) {tester}
  }
}
