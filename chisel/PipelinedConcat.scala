package foobar

import chisel3._
import chisel3.util._

class PipelinedConcat(val nelems:Int = 20, val bw:Int = 16) extends Module {

  val nelems_out = nelems*2
  val io = IO(new Bundle {
    val inA      = Input(Vec(nelems, UInt(bw.W)))
    val inB      = Input(Vec(nelems, UInt(bw.W)))
    val inAmask  = Input(UInt(nelems.W))
    val inBmask  = Input(UInt(nelems.W))
    val out      = Output(Vec(nelems_out, UInt(bw.W)))
    val outmask  = Output(UInt(nelems_out.W))
  })

  val nstages = log2Ceil(nelems) + 1
  require(nstages>1, "nstages must be 2 or greater")

  // linearlize two input vecs
  val inAB = Wire(Vec(nelems_out, UInt(bw.W)))
  for (i <- 0 until nelems) {
    inAB(i)        := io.inA(i)
    inAB(i+nelems) := io.inB(i)
  }

  // pop count of mask bits, which can tell us the length of non-zero
  // elements, assuming the elements associated to this mask are
  // sorted in order
  val popcA = PopCount(io.inAmask)
  val popcB = PopCount(io.inBmask) // not needed

  val ms = List.tabulate(nstages) {i => Module(new MaskedShift(i, nelems_out, bw, nelems_out))}

  val doshift = (popcA =/= nelems.U) && (popcB =/= 0.U)
  val nshift = Mux(doshift, nelems.U - popcA, 0.U)

  //
  //

  ms(nstages-1).io.in_vec     := inAB
  ms(nstages-1).io.in_masklen := popcA
  ms(nstages-1).io.in_opaq    := Cat(io.inBmask, io.inAmask)  // Cat(MSB, LSB)
  ms(nstages-1).io.in_nshift  := nelems.U - popcA

  for (stage <- nstages-1 to 1 by -1) {
    ms(stage-1).io.in_vec     := ms(stage).io.out_vec
    ms(stage-1).io.in_masklen := ms(stage).io.out_masklen
    ms(stage-1).io.in_opaq    := ms(stage).io.out_opaq
    ms(stage-1).io.in_nshift  := ms(stage).io.out_nshift
  }

  io.out     := ms(0).io.out_vec
  io.outmask := ms(0).io.out_opaq
}
