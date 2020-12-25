//
// test driver
//
// written by Kazutomo Yoshii <kazutomo.yoshii@gmail.com>
// 
package foobar

import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}
import chisel3.experimental._

object TestMain extends App {
  // default params and component target list
  val targetlist = List(
    "rev", "foo", "count",
    "concat", "xnor", "clz", "srmem", "nerd", "count",
    "bmsort", "catnz", "nway"  )

  val a = if (args.length > 0) args(0) else "rev"
  val tmp = a.split(":")
  val target = tmp(0)
  val mode = if (tmp.length > 1) tmp(1) else "test"

  if ( target=="list" ) {
    println("*target list")
    for (t <- targetlist)  println(t)
    System.exit(0)
  }

  // check see if only verilog output
  val verilogonly = mode.toLowerCase().substring(0,1) match {case "v" => true ; case _ => false}

  println(f"MODE=$mode TARGET=$target")

  // this function is called from each test driver
  def driverhelper[T <: MultiIOModule](dutgen : () => T, testergen: T => PeekPokeTester[T]) {
    // Note: is chisel3.Driver.execute a valid way, or
    // better to use (new ChiselStage).emitVerilog()?
    if (verilogonly) chisel3.Driver.execute(args, dutgen)
    else             iotesters.Driver.execute(args, dutgen) {testergen}
  }

  target match {
    case "list" =>
      println("*target list")
      for (t <- targetlist)  println(t)
    case "rev" => RevTest.run()
    case "foo" => FooTest.run()
    case "count" => CounterTest.run()
    case "nerd" => NerdCounterTest.run()
    case "xnor" => XnorPopTest.run()
    case "clz" => ClzTest.run()
    case "srmem" => SRMemTest.run()
    case "concat" => ConcatVecsTest.run()
    case "bmsort" => BitMaskSortedTest.run()
    case "mmsort" => MMSortTwoTest.run()
    case "catnz" => ConcatZeroStripTest.run()
    case "nway" => NwayMuxTest.run()
    case "gray" => GrayTest.run()
    case _ => RevTest.run()
  }
}
