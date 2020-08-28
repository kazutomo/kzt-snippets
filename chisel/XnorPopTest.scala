package foobar

// import chisel3._  // this causes errors.
import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}

class XnorPopUnitTester(c: XnorPop) extends PeekPokeTester(c) {

  val seed = 123
  val r = new scala.util.Random(seed)
  val ntests = 4

  val ninputs =  c.ninputs
  val mask = (1 << ninputs) - 1

  for (i <- 0 until ntests) {
    val vin = r.nextInt & mask
    val weights = List.tabulate(ninputs) { i => r.nextInt & mask }
    poke(c.io.vin, vin)
    for(j <- 0 until ninputs) {
      poke(c.io.weights(j), weights(j))
    }
    step(1)
    for(j <- 0 until ninputs) {
      val output = peek(c.io.vout(j)).toInt // peek() returns bigInt

      val ref = (~(vin^weights(j))) & mask
      val reftmp = List.tabulate(ninputs) { i => if ((ref & (1<<i))>0) 1 else 0 }
      val refcnt = reftmp.reduce {_ + _} - (ninputs>>1)
      expect(c.io.vout(j), refcnt)
      printf("%3d/%3d => cnt=%3d (%3d)\n",  i, j, output, refcnt)
    }
    printf("\n")
  }
}
