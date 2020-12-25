package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}

class FooUnitTester(c: Foo) extends PeekPokeTester(c) {

  val ntests = 10

  for (i <- 0 until ntests) {

    poke(c.io.in, i)
    
    if (i>0)  expect(c.io.out, i-1)
    else      expect(c.io.out, 0)

    printf("%08d => %08d\n",  i, peek(c.io.out).toInt)

    step(1)
  }
}

object FooTest {
  val bitwidth = 16
  val dut = () => new Foo(bitwidth)
  val tester = c => new FooUnitTester(c)

  def run() { TestMain.driverhelper(dut, tester) }
}
