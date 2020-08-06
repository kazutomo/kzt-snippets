package foobar

import chisel3._
import chisel3.util._  // to use PopCount and log2Ceil

class XnorPop(val bw:Int = 8) extends Module {
  val io = IO(new Bundle {
    val in_a  = Input(UInt(bw.W))
    val in_b  = Input(UInt(bw.W))
    val out = Output(UInt((log2Ceil(bw)+1).W))
  })

  io.out := PopCount( ~(io.in_a ^ io.in_b) )
}
