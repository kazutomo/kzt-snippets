package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}
import testutil._

class MaskedShiftUnitTester(c: MaskedShift) extends PeekPokeTester(c) {
  val maxlen = c.maxlen
  val hmaxlen = maxlen/2 // half size
  val bw = c.bw
  val shiftbit = c.shiftbit
  val _nshift = 1 << shiftbit
  val allshiftbits = (1<<log2Ceil(maxlen+1))-1

  def testnshift(doshift: Boolean, masklen: Int)  {
    val dat = List.tabulate(maxlen) {i =>
      if (i < masklen) i+10
      else if (i < hmaxlen) 0
      else i+20
    }

    // set inputs
    for (i <- 0 until maxlen) poke(c.io.in_vec(i),dat(i))
    val (nshift, in_nshift) = if (doshift) (_nshift, allshiftbits) else (0,0)

    poke(c.io.in_nshift, in_nshift)
    poke(c.io.in_masklen, masklen)
    step(1)

    val ref = List.tabulate(maxlen) {i =>
      if (i < masklen) dat(i)
      else if (i+nshift < maxlen) dat(i+nshift)
      else 0
    }

    println("-----------------------")
    println(f"shiftbit=$shiftbit doshift=$doshift nshift=$nshift")
    print("in  : ")
    for (i <- 0 until maxlen) print(f"${dat(i)}%3d ")
    println()
    print("ref : ")
    for (i <- 0 until maxlen) print(f"${ref(i)}%3d ")
    println()
    print("out : ")
    for (i <- 0 until maxlen) {
      val d = peek(c.io.out_vec(i))
      print(f"$d%3d ")
    }
    println()

    print("validated: ")
    for (i <- 0 until maxlen) {
      expect(c.io.out_vec(i), ref(i))
    }
    print("out_vec ")
    expect(c.io.out_masklen,  masklen)
    print("out_masklen ")
    expect(c.io.out_nshift, in_nshift)
    print("out_nshift ")
    println("")
  }

  testnshift(false, hmaxlen)
  for (i <- 0 to hmaxlen) {
    testnshift(true, i)
  }
}


object MaskedShiftUnitTest {
  def run(args: Array[String]) {
    val (args2, shiftbit) = TestUtil.getoptint(args,  "s", 0)
    val (args3, maxlen)   = TestUtil.getoptint(args2, "n", 10)
    val (args4, bw)       = TestUtil.getoptint(args3, "bw", 8)

    val dut = () => new MaskedShift(shiftbit, maxlen, bw, maxlen)
    val tester = c => new MaskedShiftUnitTester(c)
    TestUtil.driverhelper(args4, dut, tester)
  }
}
