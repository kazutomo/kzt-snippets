package foobar

import chisel3._
import chisel3.util._   // needed for switch

// Counting leading zeros for two-bit unsigned integer
class Clz2() extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(2.W))
    val out = Output(UInt(2.W))
  })

  io.out := 0.U

  switch(io.in) {
    is("b00".U) { io.out := "b10".U }
    is("b01".U) { io.out := "b01".U }
    is("b10".U) { io.out := "b00".U }
    is("b11".U) { io.out := "b00".U }
  }
}

// A parameterized version of counting leading-zeros
// note: nb is the power of two number that is greater-equal than 4.
class ClzParam(nb: Int = 16) extends Module {

  val half = nb >> 1
  val lognb = log2Ceil(nb)

  val io = IO(new Bundle {
    // input data
    val in  = Input(UInt(nb.W))
    // the number of the leading zeros (Nlz) of the input data, which
    // requires (log2Ceil(nb)+1) bits to hold the number. e.g., a
    // 16-bit input, the number of the leading-zeros is between 0 and
    // 16 (all bits are zeros), which requires 5 bits.
    val out = Output(UInt((lognb+1).W))
  })

  // divide 'in' into two blocks: c0 (lower half) and c1 (upper half)
  // when the half size is 2, instantiate Clz2(), otherwise
  // ClzParam(), the same class
  val c0 = if (half==2) Module(new Clz2()) else Module(new ClzParam(half))
  val c1 = if (half==2) Module(new Clz2()) else Module(new ClzParam(half))
  c0.io.in := io.in(half-1, 0)
  c1.io.in := io.in(nb-1  , half)

  io.out := 0.U((lognb+1).W)

  // If both two divided blocks only contain zero or MSB of the
  // counting number of both blocks are one, the original input 'in'
  // should only contain zero.
  when( c1.io.out(lognb-1) && c0.io.out(lognb-1) ) {
    // All-zero case. simple set MSB of io.out one.
    io.out := Cat("b1".U, 0.U(lognb.W))
  } .elsewhen( c1.io.out(lognb-1) === 0.U ) {
    // the upper half contains one or more 1, so 'io.out' is simply
    // Nlz of the upper half, regardless of the content of the lower
    // half.
    io.out := Cat("b0".U, c1.io.out)
  } .elsewhen( c1.io.out(lognb-1) === 1.U ) {
    // the upper half only contain zero while the lower half is not
    // all-zero. 'io.out' is the upper half Nlz plus the lower half
    // Nlz.
    io.out := Cat("b01".U, c0.io.out(lognb-2,0))
  }
}


//
// below are old hard-coded version. unused.
//

class Clz4() extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(4.W))
    val out = Output(UInt(3.W))
  })

  val c0 = Module(new Clz2())
  val c1 = Module(new Clz2())

  c0.io.in := io.in(1,0)
  c1.io.in := io.in(3,2)

  io.out := "b000".U
  when( c1.io.out(1) && c0.io.out(1) ) {
    io.out := "b100".U
  } .elsewhen( c1.io.out(1) === 0.U ) {
    io.out := Cat("b0".U, c1.io.out)
  } .elsewhen( c1.io.out(1) === 1.U ) {
    io.out := Cat("b01".U, c0.io.out(0,0))
  }
}


class Clz8() extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(8.W))
    val out = Output(UInt(4.W))
  })

  val c0 = Module(new Clz4())
  val c1 = Module(new Clz4())

  c0.io.in := io.in(3,0)
  c1.io.in := io.in(7,4)

  io.out := "b0000".U
  when( c1.io.out(2) && c0.io.out(2) ) {
    io.out := "b1000".U
  } .elsewhen( c1.io.out(2) === 0.U ) {
    io.out := Cat("b0".U, c1.io.out)
  } .elsewhen( c1.io.out(2) === 1.U ) {
    io.out := Cat("b01".U, c0.io.out(1,0))
  }
  // debug
  //printf("%b c1=%b c0=%b out=%d\n", io.in, c1.io.out, c0.io.out, io.out)
}


class Clz16() extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(16.W))
    val out = Output(UInt(5.W))
  })

  val c0 = Module(new Clz8())
  val c1 = Module(new Clz8())

  c0.io.in := io.in(7,0)
  c1.io.in := io.in(15,8)

  io.out := "b00000".U
  when( c1.io.out(3) && c0.io.out(3) ) {
    io.out := "b10000".U
  } .elsewhen( c1.io.out(3) === 0.U ) {
    io.out := Cat("b0".U, c1.io.out)
  } .elsewhen( c1.io.out(3) === 1.U ) {
    io.out := Cat("b01".U, c0.io.out(2,0))
  }
  // debug
  //printf("%b c1=%b c0=%b out=%d\n", io.in, c1.io.out, c0.io.out, io.out)
}
