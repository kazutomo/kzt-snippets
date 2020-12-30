package foobar

import chisel3._

// This module outputs a sequence of Fibonacci numbers. A new
// Fibonacci number is generated each clock. If the next number does
// not fit to the specified bit length, io.ismaxval becomes true to
// tell the caller the current number is the maximum possible number
// and the output in the next cycle becomes 0, starting over the
// Fibonacci number generation.
class Fibonacci(val bw:Int = 8) extends Module {
  val io = IO(new Bundle {
    val out = Output(UInt(bw.W))
    val ismaxfibnum = Output(Bool()) // the maximum possible Fibonacci number in bw bits
  })

  val aReg = RegInit(0.U((bw+1).W))
  val bReg = RegInit(1.U((bw+1).W))

  when(bReg(bw) === 1.U) {
    aReg := 0.U
    bReg := 1.U
  } .otherwise {
    aReg := bReg
    bReg := aReg + bReg
  }

  io.ismaxfibnum := bReg(bw)
  io.out := aReg
}
