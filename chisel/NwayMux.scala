package foobar

import chisel3._
import chisel3.util._

class NwayMux(val n:Int = 80, val bw:Int = 64) extends Module {
  val io = IO(new Bundle {
    val in  = Input(Vec(n, UInt(bw.W)))
    val nshift = Input(UInt(log2Ceil(n).W))
    val out = Output(Vec(n, UInt(bw.W)))
  })

  val in_ext = Wire(Vec(n+1, UInt(bw.W)))
  for (i <- 0 until n) in_ext(i)  := io.in(i)
  in_ext(n) := 0.U

  def createMuxLookupList(muxid : Int) : List[Int] =
    List.tabulate(n) {j => if ((muxid + j < n)) muxid + j else n}

  for (i <- 0 until n)  {
    val lookuplist = createMuxLookupList(i)
    val lookups = lookuplist.zipWithIndex.map {case (wireidx, sel) => sel.U -> in_ext(wireidx)}

    io.out(i) := MuxLookup(io.nshift, in_ext(i), lookups) // (idx, default, selecter_array)
  }
}
