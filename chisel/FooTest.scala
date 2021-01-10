package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}
import testutil._

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

  def run(args: Array[String]) {
    val (args2, bwval) = TestUtil.getoptint(args, "bw", 8)

    println("FooTest: bitwidth=" + bwval)

    val dut = () => new Foo(bwval)
    val tester = c => new FooUnitTester(c)

    TestUtil.driverhelper(args2, dut, tester)
  }
}