package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}

class NerdCounterUnitTester(c: NerdCounter) extends PeekPokeTester(c) {

  val max = c.max

  for (i <- 0 until max*2) {
//      expect(c.io.out, 0)

    printf("%08d => %08d\n",  i, peek(c.io.out).toInt)

    step(1)
  }
}

object NerdCounterTest {
  def run(args: Array[String]) {
    val maxval = 10
    val dut = () => new NerdCounter(maxval)
    val tester = c => new NerdCounterUnitTester(c)

    TestMain.driverhelper(args, dut, tester)
  }
}
