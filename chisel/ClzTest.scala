package foobar

import chisel3._
import chisel3.iotesters.PeekPokeTester
import testutil._

class ClzUnitTester(c: ClzParam) extends PeekPokeTester(c) {

  val nb = c.nb // the size of input data in bits

  val seed = 123
  val rn = new scala.util.Random(seed)

  def genNumber() : (Long, Int) = {
    val sh = rn.nextInt(nb+1) // random number between 0 and nb
    val b = if (sh == nb) 0.toLong else 1.toLong << sh
    val l = if (b < 2.toLong ) 0.toLong else rn.nextLong().abs % b
    (b + l, sh)
  }
  val ntries = 100

  def toBinStr(v: Long): String = {
    val s = v.toBinaryString
    val zslen = nb - s.length()
    val zs = "0" * zslen
    zs + s
  }

  for (s <- 0 until ntries) {
    val (in, sh) = genNumber()
    val str = toBinStr(in)
    val ref = if (sh == nb) nb else (nb-sh-1)
    print("in=" + str)
    poke(c.io.in, in)
    expect(c.io.out, ref)
    val out = peek(c.io.out)
    print(f"  out=$out\n")
    step(1)
  }
}

object ClzTest {

  def run(args: Array[String]) {
    val (argsrest, opt) = TestUtil.getopts(args,
      Map("n" -> "8") )

    val nb = opt("n").toInt
    val dut = () => new ClzParam(nb)
    val tester = c => new ClzUnitTester(c)

    TestUtil.driverhelper(argsrest, dut, tester)
  }
}
