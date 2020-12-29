package foobar

import chisel3._

class Fibonacci(val bw:Int = 8) extends Module {
  val io = IO(new Bundle {
    val out = Output(UInt(bw.W))
    val overflow = Output(Bool())
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

  io.overflow := bReg(bw)
  io.out := aReg
}
