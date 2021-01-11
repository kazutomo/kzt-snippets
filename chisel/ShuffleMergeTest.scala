//
// ShuffleMerge module tester
//
// written by Kazutomo Yoshii <kazutomo.yoshii@gmail.com>
//
package foobar

import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}
import testutil._

class ShuffleMergeUnitTester(c: ShuffleMerge) extends PeekPokeTester(c) {
  // input
  val nelems = c.nelems
  val elemsize = c.elemsize
  // output
  val nblocks = elemsize // the number of blocks after BitShuffle
  val bwblock = nelems   // the bitwidth of each block after BitShuffle


  for (i <- 0 until nelems ) poke(c.io.in(i), 255)


  println("* debug")
  val dm0= peek(c.io.debugmask(0)).toInt
  val dm1= peek(c.io.debugmask(1)).toInt
  println(f"mask: ${dm0}%x  ${dm1}%x")
  for (i <- 0 until 4 ) {
    val v = peek(c.io.debugA(i)).toInt
    print(f"${v}%016x ")
  }
  println()
  for (i <- 0 until 4 ) {
    val v = peek(c.io.debugB(i)).toInt
    print(f"${v}%016x ")
  }
  println()


  println("* output")
  val outmask = peek(c.io.outmask).toInt
  println( "mask: " + TestUtil.intToBinStr(outmask, nblocks))
  for (i <- 0 until nblocks ) {
    val b = peek(c.io.out(i)).toInt
    print(f"${b}%016x ") // XXX: replace 16 w/ bwblock/4
  }
  println()
}


object ShuffleMergeTest {

  def run(args: Array[String]) {

    val dut = () => new ShuffleMerge()
    val tester = c => new ShuffleMergeUnitTester(c)

    TestUtil.driverhelper(args, dut, tester)
  }
}
