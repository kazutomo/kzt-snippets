package foobar

import chisel3._
import chisel3.util._   // needed for switch

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

// crafting a paramerized version of Clz
class ClzParam(nb: Int = 16) extends Module {
  val half = nb >> 1
  val lognb = log2Ceil(nb)

  val io = IO(new Bundle {
    val in = Input(UInt(nb.W))
    val out = Output(UInt((lognb+1).W))
  })

  val c0 = if (half==2) Module(new Clz2()) else Module(new ClzParam(half))
  val c1 = if (half==2) Module(new Clz2()) else Module(new ClzParam(half))

  c0.io.in := io.in(half-1, 0)
  c1.io.in := io.in(nb-1  , half)

  io.out := 0.U((lognb+1).W)
  when( c1.io.out(lognb-1) && c0.io.out(lognb-1) ) {
    io.out := Cat("b1".U, 0.U(lognb.W))
  } .elsewhen( c1.io.out(lognb-1) === 0.U ) {
    io.out := Cat("b0".U, c1.io.out)
  } .elsewhen( c1.io.out(lognb-1) === 1.U ) {
    io.out := Cat("b01".U, c0.io.out(lognb-2,0))
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
