package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}

class MMSortTwoUnitTester(c: MMSortTwo) extends PeekPokeTester(c) {

  val bitwidth = c.bw

  val testpats = List((0,0), (0,3), (1,0), (1,3))

  def refAB(a: Int, b: Int ) : (Int, Int) = {
    if ( a == 0 )  (b, 0)
    else           (a, b)
  }

  // binarized int (to print binary with %d)
  def BI(a: Int) : Int = a.toBinaryString.toInt

  // format string
  val fs  = f"%%0${bitwidth}d"


  for (tp <- testpats) {
    poke(c.io.inA, tp._1)
    poke(c.io.inB, tp._2)

    val outA = peek(c.io.outA).toInt
    val outB = peek(c.io.outB).toInt
    val outMask = peek(c.io.outMask).toInt

    printf("A=" + fs  + " B=" + fs + " => A=" + fs + " B=" + fs + " Mask=%02d\n",
      BI(tp._1), BI(tp._2),
      outA.toBinaryString.toInt,  outB.toBinaryString.toInt,
      outMask.toBinaryString.toInt)

    val (refA, refB) = refAB(tp._1, tp._2)
    expect(c.io.outA, refA)
    expect(c.io.outB, refB)
  }

  printf("Note: mask MSB is inB and LSB is outA\n")
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
