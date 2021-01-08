package foobar

import chisel3._
import chisel3.util._  // to use PopCount and log2Ceil

// based on Nerd Counter example in the Chisel book
class NerdCounter(val max:Int = 10) extends Module {
  val nbits = log2Ceil(max+1)
  val cntstartval = max - 2

  val io = IO(new Bundle {
    val out = Output(SInt((nbits+1).W))
  })

  val cntReg = RegInit(cntstartval.S((nbits+1).W))

  cntReg := cntReg - 1.S
  when( cntReg(nbits) ) { // check the sign bit
    cntReg := cntstartval.S
  }

  io.out := cntReg + 1.S
}
