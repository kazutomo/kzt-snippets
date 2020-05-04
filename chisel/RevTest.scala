package foobar

// import chisel3._  // this causes errors.
import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}

class RevUnitTester(c: Rev) extends PeekPokeTester(c) {

  val seed = 123
  val r = new scala.util.Random(seed)
  val ntests = 10

  for (i <- 0 until ntests) {
    val input = r.nextInt(255)
    poke(c.io.in, input)
    val output = peek(c.io.out).toInt // peek() returns bigInt

    printf("%08d => %08d\n",
      input.toBinaryString.toInt,
      output.toBinaryString.toInt)

    // NOTE: add expect() here to verify
    // use c.bw
  }
}
