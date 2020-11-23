package foobar

import chisel3._
import chisel3.iotesters.PeekPokeTester

class ClzUnitTester(c: ClzParam) extends PeekPokeTester(c) {

  // val nb = c.nb

  val h = 1<<15

  for (s <- 0 until 17) {
    val in = h >> s
    poke(c.io.in, in)
    expect(c.io.out, s)
    val out = peek(c.io.out)
    print(f"in=$in%04x  out=$out\n")
    step(1)
  }
}
