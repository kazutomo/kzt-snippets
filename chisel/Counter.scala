package foobar

import chisel3._
import chisel3.util._  // to use PopCount and log2Ceil

class Counter(val max:Int = 10) extends Module {
  val nbits = log2Ceil(max+1)
  val io = IO(new Bundle {
    val out = Output(UInt(nbits.W))
  })

  val cntReg = RegInit(0.U(nbits.W))

  cntReg := cntReg + 1.U

  when( cntReg === (max-1).U) {
    cntReg := 0.U
  }

  io.out := cntReg
}
