package foobar

import chisel3._
import chisel3.util._

class NwayMux(val n:Int = 10, val bw:Int = 64) extends Module {
  val io = IO(new Bundle {
    val in  = Input(Vec(n, UInt(bw.W)))
    val nshift = Input(UInt(log2Ceil(n).W))
    val out = Output(Vec(n, UInt(bw.W)))
  })

  //val in_ext = Wire(Vec(n+1, UInt(bw.W)))
  val in_ext = RegInit(VecInit(Seq.fill(n+1)(0.U(bw.W))))

  for (i <- 0 until n)  in_ext(i) := io.in(i)
  in_ext(n) := 0.U

  val outreg = RegInit(VecInit(Seq.fill(n)(0.U(bw.W))))

  val impl = 0 // implementaion selector
               // # of lines in generated verilog codes
               // impl=0  13046
               // impl=1   6726

  if (impl == 0) {
    def createMuxLookupList(muxid : Int) : List[Int] =
      List.tabulate(n) {j => if ((muxid + j < n)) muxid + j else n}

    for (i <- 0 until n)  {
      val lookuplist = createMuxLookupList(i)
      val lookups = lookuplist.zipWithIndex.map {case (wireidx, sel) => sel.U -> in_ext(wireidx)}

      outreg(i) := MuxLookup(io.nshift, in_ext(i), lookups) // (idx, default, selecter_array)
    }
  } else {
    def createMuxLookupList(muxid : Int) : List[Int] =
      List.tabulate(n-muxid) {j => muxid + j}

    for (i <- 0 until n)  {
      val lookuplist = createMuxLookupList(i)
      val lookups = lookuplist.zipWithIndex.map {case (wireidx, sel) => sel.U -> in_ext(wireidx)}

      outreg(i) := MuxLookup(io.nshift, in_ext(n), lookups) // (idx, default, selecter_array)
    }
  }

  io.out := outreg
}
