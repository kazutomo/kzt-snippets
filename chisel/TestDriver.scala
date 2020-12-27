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

  // NOTE: I don't like lazy function eval... any better way?
  val targetmap = Map(
    "Rev"             -> (() => RevTest.run(), "bit reverse"),
    "Foo"             -> (() => FooTest.run(), "dummy"),
    "Counter"         -> (() => CounterTest.run(), "simple counter"),
    "NerdCounter"     -> (() => NerdCounterTest.run(), "nerd counter"),
    "XnorPop"         -> (() => XnorPopTest.run(), "xnor pop"),
    "Clz"             -> (() => ClzTest.run(), "leading zero count"),
    "SRMem"           -> (() => SRMemTest.run(), "sync read ram"),
    "ConcatVecs"      -> (() => ConcatVecsTest.run(), "concat two vecs"),
    "BitMaskSorted"   -> (() => BitMaskSortedTest.run(), "bit mask sorted"),
    "MMSortTwo"       -> (() => MMSortTwoTest.run(), "mask merge sort"),
    "ConcatZeroStrip" -> (() => ConcatZeroStripTest.run(), "concat zero strip"),
    "NwayMux"         -> (() => NwayMuxTest.run(), "n-way MUX"),
    "Gray"            -> (() => GrayTest.run(), "gray coding")
  )

  val a = if (args.length > 0) args(0) else "Rev"
  val tmp = a.split(":")
  val target = tmp(0)
  val mode = if (tmp.length > 1) tmp(1) else "test"

  // check see if only verilog output
  val verilogonly = mode.toLowerCase().substring(0,1) match {case "v" => true ; case _ => false}

  def printlist() {
    if ( target=="list" ) {
      println("*target list")
      for (t <- targetmap.keys) {
        printf("%-15s : %s\n", t, targetmap(t)._2)
      }
    }
  }


  val matched = targetmap.keys.filter(_.toLowerCase.matches("^" + target.toLowerCase + ".*"))

  println()
  if (matched.size == 0) {
    println("No match found for " + target)
    printlist()
    println()
    System.exit(1)
  } else if (matched.size > 1) {
    println("Multiple matches found for " + target)
    for (i <- matched) print(i + " ")
    println()
    printlist()
    println()
    System.exit(1)
  }

  val found = matched.toList(0)

  println(f"MODE=$mode TARGET=$found")

  // this function is called from each test driver
  def driverhelper[T <: MultiIOModule](dutgen : () => T, testergen: T => PeekPokeTester[T]) {
    // Note: is chisel3.Driver.execute a valid way, or
    // better to use (new ChiselStage).emitVerilog()?
    if (verilogonly) chisel3.Driver.execute(args, dutgen)
    else             iotesters.Driver.execute(args, dutgen) {testergen}
  }

  targetmap(found)._1()
}
