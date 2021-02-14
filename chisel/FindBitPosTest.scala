package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}
import testutil._

class FindBitPosUnitTester(c: FindBitPos) extends PeekPokeTester(c) {

  val bw = c.bw

  def test(v: BigInt) {
    poke(c.io.in, v)
    // model expect
    printf("pos=%d\n",  peek(c.io.out).toInt)
    step(1)
  }
  test(BigInt(0))
  
  for (i <- 0 until bw) {
    test( BigInt(1) << i )
  }
}

object FindBitPosTest {
  def run(args: Array[String]) {
    val (argsrest, bw) = TestUtil.getoptint(args, "bw", 160)

    println("FindBitPosTest: bitwidth=" + bw)

    val dut = () => new FindBitPos(bw)
    val tester = c => new FindBitPosUnitTester(c)

    TestUtil.driverhelper(argsrest, dut, tester)
  }
}
