package foobar

import chisel3._

class Bin2Gray(val bw:Int = 8) extends Module {
  val io = IO(new Bundle {
    val in  = Input(UInt(bw.W))
    val out = Output(UInt(bw.W))
  })

  require(bw >= 2)

  // converting between an interter and a bit vector
  val tmpin = Wire(Vec(bw, UInt(1.W)))
  val tmpout = Wire(Vec(bw, UInt(1.W)))

  for (i <- 0 until bw) tmpin(i) := io.in(i)

  // any better way?
  val tmpoutvec = VecInit.tabulate(bw) {i => tmpout(i) << i }
  io.out := tmpoutvec.reduce(_ | _)

  // the main logic of the bin-to-gray coding
  for (i <- 0 until bw-1) tmpout(i) := tmpin(i) ^ tmpin(i+1)
  tmpout(bw-1) := tmpin(bw-1)
}

class Gray2Bin(val bw:Int = 8) extends Module {
  val io = IO(new Bundle {
    val in  = Input(UInt(bw.W))
    val out = Output(UInt(bw.W))
  })
  require(bw >= 2)

  // converting between an interter and a bit vector
  val tmpin = Wire(Vec(bw, UInt(1.W)))
  val tmpout = Wire(Vec(bw, UInt(1.W)))

  for (i <- 0 until bw) tmpin(i) := io.in(i)

  // any better way?
  val tmpoutvec = VecInit.tabulate(bw) {i => tmpout(i) << i }
  io.out := tmpoutvec.reduce(_ | _)

  // the main logic of the gray-to-bin coding
  tmpout(bw-1) := tmpin(bw-1)
  for (i <- (bw-2) to 0 by -1)  tmpout(i) := tmpout(i+1) ^ tmpin(i)
}


class Gray(val bw:Int = 8) extends Module {
  val io = IO(new Bundle {
    val encode = Input(Bool()) // true => encode / false => decode
    val in  = Input(UInt(bw.W))
    val out = Output(UInt(bw.W))
  })

  val b2g = Module(new Bin2Gray(bw))
  val g2b = Module(new Gray2Bin(bw))

  b2g.io.in := io.in
  g2b.io.in := io.in

  io.out := Mux(io.encode === 1.U, b2g.io.out, g2b.io.out)
}
