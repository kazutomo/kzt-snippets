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
  // NOTE: It is possible to register objects directly to this map,
  // which requires less type, however, the constructor of all objects
  // are called here, which is not preferable. With this way, the
  // constructor of test object is not called until the run() method
  // is called, which is good.
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
    "Gray"            -> (() => GrayTest.run(), "gray coding"),
    "Fibonacci"       -> (() => FibonacciTest.run(), "Fibonacci number")
  )

  def printlist() {
    println("*target list")
    for (t <- targetmap.keys) {
      printf("%-15s : %s\n", t, targetmap(t)._2)
    }
  }

  if (args.length < 2) {
    println("Usage: foobar.TestMain mode target [options]")
    println("")
    System.exit(1)
  }

  val mode   = args(0)
  val target = args(1)

  def checkfirstcharnocap(s: String, c: String) : Boolean = if (s.toLowerCase().substring(0,1) == c ) true else false

  // check see if only verilog output
  val verilogonly = checkfirstcharnocap(mode, "v")

  if (checkfirstcharnocap(mode, "l")) {
    printlist()
    System.exit(0)
  }

  // find target module name match
  val matched = targetmap.keys.filter(
    _.toLowerCase.matches("^" + target.toLowerCase + ".*"))

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
    // Note: is chisel3.Driver.execute a right way to generate
    // verilog, or better to use (new ChiselStage).emitVerilog()?
    if (verilogonly) chisel3.Driver.execute(args, dutgen)
    else             iotesters.Driver.execute(args, dutgen) {testergen}
  }

  targetmap(found)._1()
}
