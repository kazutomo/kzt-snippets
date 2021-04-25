package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}
import testutil._

class HoldUnitTester(c: Hold) extends PeekPokeTester(c) {

  val bw = c.bw
  val n = c.n
  val ntests = 20

  for (i <- 0 until ntests) {
    val in = i+1
    poke(c.io.d_in, in)

    val outstr = List.tabulate(n) {i => TestUtil.convIntegerToHexStr(peek(c.io.d_out(i)), bw*n) } reduce(_+_)
    val valid = peek(c.io.d_out_valid)
    printf(f"${i}%2d: ${in}%02x => $outstr $valid\n")

    step(1)
  }
}

object HoldTest {

  def run(args: Array[String]) {
    val (args2, bwval) = TestUtil.getoptint(args, "bw", 8)

    println("HoldTest: bitwidth=" + bwval)

    val dut = () => new Hold(bwval)
    val tester = c => new HoldUnitTester(c)

    TestUtil.driverhelper(args2, dut, tester)
  }
}
