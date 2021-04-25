package foobar

import chisel3._
import chisel3.util.log2Ceil
/** Combine inputs over time and output combined inputs when the
  * internal hold buffer is full
  * 
  * @param bw bitwidth
  * @param n  the number of inputs
  */

class Hold(val bw:Int = 16, val n: Int = 4) extends Module {
  /**
    * d_in  : bw-bit input signal 
    * d_out : (n*bw)-bit output signal
    * d_out_valid : when the hold buffer gets full. only valid one cycle
    */
  val io = IO(new Bundle {
    val d_in  = Input(UInt(bw.W))
    val d_out = Output(Vec(n, UInt(bw.W)))
    val d_out_valid = Output(Bool())
  })

  val holdbuf = RegInit(VecInit(Seq.fill(n)(0.U(bw.W))))
  val pos = RegInit(0.U(log2Ceil(n+1).W))
  val dvalid = RegInit(false.B)

  when (pos === 0.U) {
    holdbuf(0) := io.d_in
    for (i <- 1 until n) holdbuf(i) := 0.U
  } .otherwise {
    holdbuf(pos) := io.d_in
  }

  when (pos < (n-1).U) {
    dvalid := false.B
    pos := pos + 1.U
  } .otherwise {
    dvalid := true.B
    pos := 0.U
  }

  io.d_out_valid := dvalid
  io.d_out := holdbuf
}
