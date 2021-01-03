package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}

class CounterUnitTester(c: Counter) extends PeekPokeTester(c) {

  val max = c.max

  val ntests = 10

  for (i <- 0 until max*2 + 2) {
    expect(c.io.out, i % max)
    printf("%08d => %08d\n",  i, peek(c.io.out).toInt)
    step(1)
  }
}

object CounterTest {
  def run(args: Array[String]) {
    val maxval = 10
    val dut = () => new Counter(maxval)
    val tester = c => new CounterUnitTester(c)

    TestMain.driverhelper(args, dut, tester)
  }
}
