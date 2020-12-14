package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}

class MMSortTwoUnitTester(c: MMSortTwo) extends PeekPokeTester(c) {

  val bitwidth = c.bw

  val testpats = List((0,0), (0,3), (1,0), (1,3))

  def refAB(a: Int, b: Int ) : Int = {
    if ( a == 0 )  b
    else a | (b << bitwidth)
  }

  // format string
  val fs_in  = f"%%0${bitwidth}d"
  val fs_out = f"%%0${bitwidth*2}d"

  for (tp <- testpats) {
    poke(c.io.inA, tp._1)
    poke(c.io.inB, tp._2)

    val outAB = peek(c.io.outAB).toInt
    val outMask = peek(c.io.outMask).toInt

    printf("A=" + fs_in  + " B=" + fs_in + " => AB=" + fs_out + " Mask=%02d\n",
      tp._1, tp._2, outAB.toBinaryString.toInt, outMask.toBinaryString.toInt)
  }
}

object MMSortTwoTest {
  val bitwidth = 4
  val dut = () => new MMSortTwo(bitwidth)
  val tester = c => new MMSortTwoUnitTester(c)

  def run(args: Array[String], verilogonly: Boolean)  {
    if (verilogonly)
      chisel3.Driver.execute(args, dut)
    else
      iotesters.Driver.execute(args, dut) {tester}
  }
}
