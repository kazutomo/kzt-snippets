package foobar

import chisel3._
import chisel3.util._

/*
 Concatenate the content of the vector 'a' and the vector 'b'.  The
 length of the vector 'a' is between 1 and 3 and the length of the
 vector 'b' is fixed (8 by default). The length of the output vector
 is fixed, which is alenmax + blen. If the length of 'a' is less than
 three, 0 is filled in unused elements.  This module can be used for
 creating a data packet with variable-sized header.
 */
 
class ConcatVecs(val alenmax:Int = 3, val blen:Int =8, val nbits:Int = 8 ) extends Module {
  val io = IO(new Bundle {
    val alen  = Input(UInt(8.W)) // should be 1, 2 or 3
    val a  = Input(Vec(alenmax, UInt(nbits.W)))
    val b  = Input(Vec(blen, UInt(nbits.W)))
    val out = Output(Vec(alenmax+blen,UInt(nbits.W)))
  })

  when (io.alen === 3.U) {
    io.out(0) := io.a(0)
    io.out(1) := io.a(1)
    io.out(2) := io.a(2)
    for(i <- 0 until blen)  io.out(i+3) := io.b(i)
  } .elsewhen (io.alen === 2.U) {
    io.out(0) := io.a(0)
    io.out(1) := io.a(1)
    for(i <- 0 until blen) io.out(i+2) := io.b(i)
    io.out(alenmax+blen-1) := 0.U
  } .otherwise {
    // Note: assume that alen is 1 here, but it should be checked

    io.out(0) := io.a(0)
    for(i <- 0 until blen) io.out(i+1) := io.b(i)
    io.out(alenmax+blen-1) := 0.U
    io.out(alenmax+blen-2) := 0.U
 }
}
