package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}

class FibonacciUnitTester(c: Fibonacci) extends PeekPokeTester(c) {

  val ntests = 100

  val bw = c.bw
  require((bw < 32) && (bw > 0))

  val maxval = (1<<bw)-1

  var a = 0
  var b = 1

  for (i <- 0 until ntests) {
    // no input to this module
    expect(c.io.out, a)
    val tmp = a + b
    a = b
    b = tmp
    if (a > maxval) {
      expect(c.io.ismaxfibnum, 1)
      a = 0
      b = 1
    } else {
      expect(c.io.ismaxfibnum, 0)
    }

    printf("%d => %d %d\n",  i, peek(c.io.out).toInt, peek(c.io.ismaxfibnum).toInt)
    step(1)
  }
}

object FibonacciTest {

  def run(args: Array[String]) {
    val bitwidth = 7
    val dut = () => new Fibonacci(bitwidth)
    val tester = c => new FibonacciUnitTester(c)
    TestMain.driverhelper(args, dut, tester)
  }
}
