package foobar

import chisel3._
import chisel3.util._  // to use PopCount and log2Ceil

/*
 A simple code that uses SyncReadMem()
 */
class SRMem(val n_entries: Int = 16, val wid: Int = 16) extends Module {
  val io = IO(new Bundle {
    val wEnable   = Input(Bool())
    val wData     = Input(UInt(wid.W))
    val wCnt      = Output(UInt(log2Ceil(n_entries).W))
    val wCntReset = Input(Bool())
    val rAddr     = Input(UInt(log2Ceil(n_entries).W))
    val rData     = Output(UInt(wid.W))
  })

  val cntReg =  RegInit(0.U(log2Ceil(n_entries).W))

  io.wCnt := cntReg

  val srmem = SyncReadMem(n_entries, UInt(wid.W))

  io.rData := Mux(io.rAddr < cntReg, srmem.read(io.rAddr), 0.U)


  when (io.wEnable && (cntReg < n_entries.U) ) {
    srmem.write(cntReg, io.wData)
    cntReg := cntReg + 1.U
  }

  when (io.wCntReset) {
    cntReg := 0.U
  }
}
