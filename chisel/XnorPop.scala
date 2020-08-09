package foobar

import chisel3._
import chisel3.util._  // to use PopCount and log2Ceil

class XnorPop(val ninputs:Int = 8) extends Module {
  val io = IO(new Bundle {
    val vin     = Input( UInt(ninputs.W))
    val weights = Input( Vec(ninputs,UInt(ninputs.W)))
    val vout    = Output(Vec(ninputs,UInt((log2Ceil(ninputs)+1).W)))
  })

  for (i <- 0 until ninputs) {
    io.vout(i) := PopCount( ~(io.vin ^ io.weights(i)) )
  }
}
