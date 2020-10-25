package foobar

// import chisel3._  // this causes errors.
import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}

class SRMemUnitTester(c: SRMem) extends PeekPokeTester(c) {

  val n_entries =  c.n_entries
  val unused_entries = 6
  val wid = c.wid

  poke(c.io.wEnable, 1)

  for (i <- 0 until n_entries - unused_entries) {
    poke(c.io.wData, i + 100)
    expect(c.io.wCnt, i)
    step(1)
  }
  poke(c.io.wEnable, 0)

  val n = peek(c.io.wCnt).toInt

  for (i <- 0 until n_entries) {
    poke(c.io.rAddr, i)
    step(1)
    if (i < n) {
      expect(c.io.rData, i + 100)
    } else {
      expect(c.io.rData, 0)
    }
  }
}
