package foobar

import chisel3._
import chisel3.util._  // to use Reverse()

class Rev(val bw:Int = 8) extends Module {
  val io = IO(new Bundle {
    val in  = Input(UInt(bw.W))
    val out = Output(UInt(bw.W))
  })

  io.out := Reverse(io.in)
}
