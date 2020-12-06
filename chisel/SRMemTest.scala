package foobar

// import chisel3._  // this causes errors.
import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}

class SRMemUnitTester(c: SRMem) extends PeekPokeTester(c) {

  val n_entries =  c.n_entries
  val unused_entries = 6
  val wid = c.wid

  def datafunc(a: Int) = a + 100

  def writepat() {
    poke(c.io.wEnable, 1)
    for (i <- 0 until n_entries - unused_entries) {
      poke(c.io.wData, datafunc(i))
      expect(c.io.wCnt, i)
      step(1)
    }
    poke(c.io.wEnable, 0)
  }
  writepat()
  poke(c.io.wCntReset, 1)
  step(1)
  poke(c.io.wCntReset, 0)
  step(1)
  expect(c.io.wCnt, 0)

  writepat()
  step(1)

  val n = peek(c.io.wCnt).toInt

  for (i <- 0 until n_entries) {
    poke(c.io.rAddr, i)
    step(1)
    if (i < n) {
      expect(c.io.rData, datafunc(i))
    } else {
      expect(c.io.rData, 0)
    }
  }
}


object SRMemTest {
  val dut = () => new SRMem()
  val tester = c => new SRMemUnitTester(c)

  def run(args: Array[String], verilogonly: Boolean)  {
    if (verilogonly)
      chisel3.Driver.execute(args, dut)
    else
      iotesters.Driver.execute(args, dut) {tester}
  }
}
