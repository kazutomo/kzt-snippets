package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}

class GrayUnitTester(c: Gray) extends PeekPokeTester(c) {

  val bw = c.bw
  require(bw >=2 && bw < 32)

  val maxval = (1 << bw) - 1

  val seed = 123
  val r = new scala.util.Random(seed)
  val ntests = 10

  for (i <- 0 until ntests) {
    val vin = r.nextInt(maxval)

    poke(c.io.encode, 1)
    poke(c.io.in, vin)
    val vinen = peek(c.io.out).toInt

    poke(c.io.encode, 0)
    poke(c.io.in, vinen)
    val vinende = peek(c.io.out).toInt
    expect(c.io.out, vin)

    printf("test%03d: in=%02x en=%02x de=%02x\n", i, vin, vinen, vinende)
    step(1)
  }
}


object GrayTest {

  def run(args: Array[String]) {
    val bitwidth = 4
    val dut = () => new Gray(bitwidth)
    val tester = c => new GrayUnitTester(c)

    TestMain.driverhelper(args, dut, tester)
  }
}
