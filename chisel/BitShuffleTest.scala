//
// BitShuffle module tester
//
// written by Kazutomo Yoshii <kazutomo.yoshii@gmail.com>
//
package foobar

import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}
import testutil._

class BitShuffleUnitTester(c: BitShuffle) extends PeekPokeTester(c) {

  val n = c.nelems
  val b = c.elemsize
  require(n <= 64) // what is the minimum?
  require(b <= 32)

  val maxval = (1<<b) - 1

  val ntests = 10 // the number of tests
  var epoch = 0

  val seed = 123
  val r = new scala.util.Random(seed)

  // def pickval(pos: Int) : Int =  pos%2
  def pickval(pos: Int) : Int = {
    val v = r.nextInt(1000)
    val m = List.tabulate(5)(idx => idx*100+100)
    if (v < m(0)) 0
    else if (v >= m(0) && v < m(1)) 1
    else if (v >= m(1) && v < m(2)) 2
    else if (v >= m(2) && v < m(3)) 3
    else if (v >= m(3) && v < m(4)) 4
    else r.nextInt(maxval+1-5) + 5
  }

  def geninputdata() : List[Int] = { // note: assume the input data size
    epoch += 1
    if (epoch == 1 ) List.tabulate(n)(i => 0)
    else if (epoch == 2) List.tabulate(n-1)(i =>0) ::: List(1)
    else if (epoch == 3) List.tabulate(n-1)(i =>0) ::: List(maxval)
    else
      List.tabulate(n)(i => pickval(i))
  }

  // if the sh'th bit of v is 1, it returns 1, otherwise returns 0
  def bittest(v: Int, sh: Int) : BigInt = if( (v & (1<<sh)) == 0) BigInt(0) else BigInt(1)

  for(t <- 0 until ntests) {
    val data = geninputdata()
    val shuffled =
      List.tabulate(b) {bpos =>
        List.tabulate(n) {idx => bittest(data(idx),bpos)<<idx} reduce(_|_)
      }

    println("REF:")
    shuffled foreach {v => print(f"$v%04x ")}
    println()

    print("IN :")
    for (i <- 0 until n) {
      val tmp = data(i)
      print(f"$tmp%04x ")
      poke(c.io.in(i), tmp)
    }
    println()
    print("OUT:")
    for (j <- 0 until b) {
      val tmp = peek(c.io.out(j))
      print(f"$tmp%04x ")
      expect(c.io.out(j), shuffled(j))
    }
    println()
  }
}


object BitShuffleTest {

  def run(args: Array[String]) {

    val dut = () => new BitShuffle()
    val tester = c => new BitShuffleUnitTester(c)

    TestUtil.driverhelper(args, dut, tester)
  }
}
