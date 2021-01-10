//
// ShuffleMerge
//
// written by Kazutomo Yoshii <kazutomo.yoshii@gmail.com>
//
package foobar

import chisel3._

// ShuffleMerge16
class ShuffleMerge(val nelems:Int = 64, val elemsize:Int = 8) extends Module {

  val nblocks = elemsize // the number of blocks after BitShuffle
  val bwblock = nelems   // the bitwidth of each block after BitShuffle

  // Add require() here

  val io = IO(new Bundle {
    val in  = Input( Vec(nelems,  UInt(elemsize.W)))
    val out = Output(Vec(nblocks, UInt(bwblock.W)))
    val outmask = Output(UInt(elemsize.W))
  })

  val sh = Module(new BitShuffle(nelems, elemsize))

  for (i <- 0 until nelems) sh.io.in(i) := io.in(i)


  val msort = Array.fill(4) { Module(new MMSortTwo(nelems)) }
  val concat2in = Array.fill(2) { Module(new ConcatZeroStrip(2, bwblock)) }
  val concat4in = Module(new ConcatZeroStrip(4, bwblock))

  for (i <- 0 until nblocks by 2) {
    val bidx = i/2
    msort(bidx).io.inA := sh.io.out(i)
    msort(bidx).io.inB := sh.io.out(i+1)
  }

  concat2in(0).io.inA     := msort(0).io.out
  concat2in(0).io.inAmask := msort(0).io.outMask
  concat2in(0).io.inB     := msort(1).io.out
  concat2in(0).io.inBmask := msort(1).io.outMask

  concat2in(1).io.inA     := msort(2).io.out
  concat2in(1).io.inAmask := msort(2).io.outMask
  concat2in(1).io.inB     := msort(3).io.out
  concat2in(1).io.inBmask := msort(3).io.outMask

  concat4in.io.inA     := concat2in(0).io.out
  concat4in.io.inAmask := concat2in(0).io.outmask
  concat4in.io.inB     := concat2in(1).io.out
  concat4in.io.inBmask := concat2in(1).io.outmask

  for (i <- 0 until nblocks)  io.out(i) := concat4in.io.out(i)

  io.outmask := concat4in.io.outmask
}
