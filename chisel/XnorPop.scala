package foobar

import chisel3._
import chisel3.util._  // to use PopCount and log2Ceil

// An implementation of XnorPop count operation used in Binarized Neural Network
class XnorPop(val ninputs:Int = 8) extends Module {
  val io = IO(new Bundle {
    val vin     = Input( UInt(ninputs.W))
    val weights = Input( Vec(ninputs,UInt(ninputs.W)))
    val vout    = Output(Vec(ninputs,SInt(log2Ceil(ninputs).W)))
  })

  for (i <- 0 until ninputs) {
    val tmp = PopCount( ~(io.vin ^ io.weights(i)) ).asSInt

    // the result is the number between +N and -N
    io.vout(i) := tmp - (ninputs >> 1).S
  }
}
