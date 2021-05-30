package foobar

import chisel3._
import chisel3.util._  // to use PopCount and log2Ceil

class Counter(val max:Int = 10) extends Module {
  val nbits = log2Ceil(max+1)
  val io = IO(new Bundle {
    val enable = Input(Bool())
    val out = Output(UInt(nbits.W))
  })

  val cntReg = RegInit(0.U(nbits.W))

  cntReg := Mux(cntReg === max.U, 0.U,
    Mux(io.enable, cntReg + 1.U, cntReg))

  io.out := cntReg
}
