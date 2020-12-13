package foobar

import chisel3._
import chisel3.util._  // to use PopCount and log2Ceil

// bitwise sort
// e.g., in: 01011 => out: 00111

class BitMaskSorted(val bw:Int = 10) extends Module {
  val io = IO(new Bundle {
    val in  = Input(UInt(bw.W))
    val out = Output(UInt(bw.W))
  })

  val tmp = Wire(UInt((bw+1).W))

  tmp := (1.U << PopCount(io.in)) - 1.U

  io.out := tmp
}


