package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}

class BitMaskSortedUnitTester(c: BitMaskSorted) extends PeekPokeTester(c) {

  val bitwidth = c.bw
  val maxval = (1 << c.bw) - 1

  val ntries = 10
  val seed = 123
  val rn = new scala.util.Random(seed)

  // note: this works 2-bit or greater and up to 32-bit due to Int
  def bitsorted(v: Int) : Int = {
    val t = List.tabulate(bitwidth) { i => if((v&(1<<i)) != 0) 1 else 0 }
    val pc = t.reduce(_ + _)
    (1 << pc) - 1
  }

  // assume that bitwidth is at least 8
  val testpats = List(0, 1, 3, 5, 85, 255)
  val randomtestpats = List.tabulate(ntries) { i => rn.nextInt(maxval+1) }

  val fs = f"%%0${bitwidth}d"

  for (tp <- testpats ::: randomtestpats) {
    poke(c.io.in, tp)
    val out = peek(c.io.out).toInt

    printf(fs + " => " + fs + " " + fs + "\n",
      tp.toBinaryString.toInt,
      out.toBinaryString.toInt,  bitsorted(tp).toBinaryString.toInt)
  }
}

object BitMaskSortedTest {
  val bw = 10
  val dut = () => new BitMaskSorted(bw)
  val tester = c => new BitMaskSortedUnitTester(c)

  def run(args: Array[String], verilogonly: Boolean)  {
    if (verilogonly)
      chisel3.Driver.execute(args, dut)
    else
      iotesters.Driver.execute(args, dut) {tester}
  }
}
