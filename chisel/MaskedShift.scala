package foobar

import chisel3._
import chisel3.util._

/** An instance of MaskedShift() is a pipeline stage in
  * PipelinedConcat()
  * 
  * @in_vec      input vector
  * @in_masklen  the input vector contents [0, masklen) are simply copied to out_vec
  * @in_opaq     holds the header data such as bitmask
  * @in_nshift   the number of shift
  * @out_vec     shifted masked output based on in_vec
  * @out_masklen one cycle delayed of in_masklen
  * @out_opaq    one cycle delayed of in_opaq
  * @out_nshift  one cycle delayed of in_nshift
  * 
  */
class MaskedShift(val shiftbit:Int = 0, val maxlen:Int = 10, val bw:Int = 16, val opaqbw:Int = 10) extends Module {

  // require()

  val io = IO(new Bundle {
    val in_vec      = Input(Vec(maxlen, UInt(bw.W)))
    val in_masklen  = Input(UInt(log2Ceil(maxlen+1).W))
    val in_opaq     = Input(UInt(opaqbw.W))
    val in_nshift   = Input(UInt(log2Ceil(maxlen+1).W))
    val out_vec     = Output(Vec(maxlen, UInt(bw.W)))
    val out_masklen = Output(UInt(log2Ceil(maxlen+1).W))
    val out_opaq    = Output(UInt(opaqbw.W))
    val out_nshift  = Output(UInt(log2Ceil(maxlen+1).W))
    // note: technically masklen and nshift only require
    // log2Ceil((maxlen/2)+1). However, (i.U + nshift) requires
    // log2Ceil(maxlen+1).
  })

  val o_vecreg = RegInit(VecInit(Seq.fill(maxlen)(0.U(bw.W))))
  val o_masklenreg = RegNext(io.in_masklen, 0.U)
  val o_opaqreg = RegNext(io.in_opaq, 0.U)
  val o_nshiftreg = RegNext(io.in_nshift, 0.U)

  io.out_vec := o_vecreg
  io.out_masklen := o_masklenreg
  io.out_opaq := o_opaqreg
  io.out_nshift := o_nshiftreg

  val shifted_in_vec = Wire(Vec(maxlen, UInt(bw.W)))
  val fixedshift = 1<<shiftbit
  for (i <- 0 until maxlen) {
    if (i + fixedshift < maxlen)
      shifted_in_vec(i) := io.in_vec(i+fixedshift)
    else
      shifted_in_vec(i) := 0.U
  }

  val nshift = io.in_nshift & (1.U<<shiftbit)

  for (i <- 0 until maxlen) {
    val cond = (nshift>0.U) && (i.U >= io.in_masklen)
    o_vecreg(i) := Mux(cond, shifted_in_vec(i), io.in_vec(i))

    /*
    when (i.U < io.in_masklen) {
      o_vecreg(i) := io.in_vec(i)
    } .otherwise {
      o_vecreg(i) := shifted_in_vec(i)
      when ( i.U < maxlen.U - nshift ) {
        o_vecreg(i) := io.in_vec(i.U + nshift)

      } .otherwise {
        o_vecreg(i) := 0.U
      }
    }
     */
  }
}
