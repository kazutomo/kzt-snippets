package foobar

import chisel3._
import chisel3.util._ // OHToUInt

class FindBitPos(val bw:Int = 160) extends Module {
  val io = IO(new Bundle {
    val in  = Input(UInt(bw.W))
    val out = Output(UInt(log2Ceil(bw).W))
  })

  io.out := PriorityEncoder(io.in)
}
