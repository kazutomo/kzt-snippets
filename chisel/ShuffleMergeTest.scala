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

}


object ShuffleMergeTest {

  def run(args: Array[String]) {

    val dut = () => new ShuffleMerge()
    val tester = c => new ShuffleMergeUnitTester(c)

    TestUtil.driverhelper(args, dut, tester)
  }
}
