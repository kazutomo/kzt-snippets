package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}
import testutil._

class MMSortTwoUnitTester(c: MMSortTwo) extends PeekPokeTester(c) {

  val bitwidth = c.bw

  val testpats = List((0,0), (0,3), (1,0), (1,3))

  def refAB(a: Int, b: Int ) : (Int, Int) = {
    if ( a == 0 )  (b, 0)
    else           (a, b)
  }
  def refMask(a: Int, b: Int ) : Int = {
    (if(a>0) 1 else 0) + (if(b>0) 2 else 0)
  }

  // binarized int (to print binary with %d)
  def BI(a: Int) : Int = a.toBinaryString.toInt

  // format string
  val fs  = f"%%0${bitwidth}d"


  for (tp <- testpats) {
    poke(c.io.inA, tp._1)
    poke(c.io.inB, tp._2)

    val outLow = peek(c.io.out(0)).toInt
    val outHigh = peek(c.io.out(1)).toInt
    val outMask = peek(c.io.outMask).toInt

    printf("B=" + fs  + " A=" + fs + " => " + fs + " " + fs + " Mask=%02d\n",
      BI(tp._2), BI(tp._1),
      outHigh.toBinaryString.toInt,  outLow.toBinaryString.toInt,
      outMask.toBinaryString.toInt)

    val (refA, refB) = refAB(tp._1, tp._2)
    expect(c.io.out(0), refA)
    expect(c.io.out(1), refB)
    expect(c.io.outMask, refMask(tp._1, tp._2))
  }

  printf("Note: mask MSB is inB and LSB is outA\n")
}

object MMSortTwoTest {

  def run(args: Array[String]) {
    val bitwidth = 4
    val dut = () => new MMSortTwo(bitwidth)
    val tester = c => new MMSortTwoUnitTester(c)
    TestUtil.driverhelper(args, dut, tester)
  }
}
