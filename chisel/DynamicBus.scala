package foobar

import chisel3._
import chisel3.util._

class DynamicBus(val amax:Int = 3, val bmax:Int =8, val nbits:Int = 8 ) extends Module {
  val io = IO(new Bundle {
    val alen  = Input(UInt(8.W)) // should be 1, 2 or 3
    val a  = Input(Vec(amax, UInt(nbits.W)))
    val b  = Input(Vec(bmax, UInt(nbits.W)))
    val out = Output(Vec(amax+bmax,UInt(nbits.W)))
  })


  when (io.alen === 3.U) {
    io.out(0) := io.a(0)
    io.out(1) := io.a(1)
    io.out(2) := io.a(2)
    for(i <- 0 until bmax)  io.out(i+3) := io.b(i)
  } .elsewhen (io.alen === 2.U) {
    io.out(0) := io.a(0)
    io.out(1) := io.a(1)
    for(i <- 0 until bmax) io.out(i+2) := io.b(i)
    io.out(amax+bmax-1) := 0.U
  } .otherwise {
    io.out(0) := io.a(0)
    for(i <- 0 until bmax) io.out(i+1) := io.b(i)
    io.out(amax+bmax-1) := 0.U
    io.out(amax+bmax-2) := 0.U
 }
}
