package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}
import testutil._

class CounterUnitTester(c: Counter) extends PeekPokeTester(c) {

  val max = c.max
  var refcnt = 0

  def test(e : Int) {
    poke(c.io.enable, e)
    for (i <- 0 until max+2) {
      expect(c.io.out, refcnt)
      printf("out=%2d ref=%2d enable=%d\n", peek(c.io.out).toInt, refcnt, e)
      step(1)
      if (e==1) {
        if(refcnt < max) refcnt += 1 else refcnt = 0
      }
    }
  }
  test(1)
  test(0)
}

object CounterTest {
  def run(args: Array[String]) {
    val maxval = 9
    val dut = () => new Counter(maxval)
    val tester = c => new CounterUnitTester(c)

    TestUtil.driverhelper(args, dut, tester)
  }
}
