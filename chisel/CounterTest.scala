package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}
import testutil._

class CounterUnitTester(c: Counter) extends PeekPokeTester(c) {

  val max = c.max
  var refcnt = 0
  val seed = 123
  val rn = new scala.util.Random(seed)

  def test(enable:Int, ncycles: Int) {
    poke(c.io.enable, enable)
    for (i <- 0 until ncycles) {
      expect(c.io.out, refcnt)
      printf("out=%2d ref=%2d enable=%d\n", peek(c.io.out).toInt, refcnt, enable)
      step(1)
      if (enable == 1) {
        if(refcnt < max) refcnt += 1 else refcnt = 0
      }
    }
  }

  test(0, 1)
  test(1, 1)
  test(0, 1)
  for (i <- 0 until 10) {
    test(1, rn.nextInt(max*3)+1) // between 1 and max*3-1
    test(0, rn.nextInt(max*3)+1) // between 1 and max*3-1
  }
}

object CounterTest {
  def run(args: Array[String]) {
    val maxval = 9
    val dut = () => new Counter(maxval)
    val tester = c => new CounterUnitTester(c)

    TestUtil.driverhelper(args, dut, tester)
  }
}
