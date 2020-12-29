package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}

class FibonacciUnitTester(c: Fibonacci) extends PeekPokeTester(c) {

  val ntests = 20

  for (i <- 0 until ntests) {
    printf("%d => %d %d\n",  i, peek(c.io.out).toInt, peek(c.io.overflow).toInt)
    step(1)
  }
}

object FibonacciTest {
  val bitwidth = 7
  val dut = () => new Fibonacci(bitwidth)
  val tester = c => new FibonacciUnitTester(c)

  def run() { TestMain.driverhelper(dut, tester) }
}
