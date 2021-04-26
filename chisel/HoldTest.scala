package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}
import testutil._

class HoldUnitTester(c: Hold) extends PeekPokeTester(c) {

  val bw = c.bw
  val maxval = (1<<bw)-1
  val n = c.n
  val seed = 123
  val r = new scala.util.Random(seed)
  val ntests = 10
  val testdata = List.tabulate(ntests*n) {i => r.nextInt(maxval)}
  val holdbuf = new Array[Int](n)
  var pos = 0


  def test(indata: Int) {
    val outstr = List.tabulate(n) {i => TestUtil.convIntegerToHexStr(peek(c.io.d_out(i)), bw) } reduce(_+_)
    printf(f"${indata}%02x => $outstr ")

    if (peek(c.io.d_out_valid)>0) {
      for (i <- 0 until n) expect(c.io.d_out(i), holdbuf(i))
      val validated = List.tabulate(n){i => peek(c.io.d_out(i)).toInt == holdbuf(i)} reduce(_&_)
      printf(f"validated=${validated}")
    }
    printf("\n")
  }

  for (indata <- testdata) {
    poke(c.io.d_in, indata)

    test(indata)

    holdbuf(pos) = indata
    if (pos<n-1)   pos += 1
    else pos = 0

    step(1)
  }

  test(0)
}

object HoldTest {

  def run(args: Array[String]) {
    val (args2, bwval) = TestUtil.getoptint(args, "bw", 8)

    println("HoldTest: bitwidth=" + bwval)

    val dut = () => new Hold(bwval)
    val tester = c => new HoldUnitTester(c)

    TestUtil.driverhelper(args2, dut, tester)
  }
}
