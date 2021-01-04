//
// test driver
//
// written by Kazutomo Yoshii <kazutomo.yoshii@gmail.com>
// 
package foobar

import testutil._

object TestMain extends App {

  if (args.length < 2) {
    println("Usage: foobar.TestMain mode target [options]")
    println("")
    System.exit(1)
  }

  val args2 = args.drop(2)

  // default params and component target list
  // key is the name of target module
  // value contains the run method function and the description
  val targetmap = Map(
    "Rev"             -> (() => RevTest.run(args2), "bit reverse"),
    "Foo"             -> (() => FooTest.run(args2), "dummy"),
    "Counter"         -> (() => CounterTest.run(args2), "simple counter"),
    "NerdCounter"     -> (() => NerdCounterTest.run(args2), "nerd counter"),
    "XnorPop"         -> (() => XnorPopTest.run(args2), "xnor pop"),
    "Clz"             -> (() => ClzTest.run(args2), "leading zero count"),
    "SRMem"           -> (() => SRMemTest.run(args2), "sync read ram"),
    "ConcatVecs"      -> (() => ConcatVecsTest.run(args2), "concat two vecs"),
    "BitMaskSorted"   -> (() => BitMaskSortedTest.run(args2), "bit mask sorted"),
    "MMSortTwo"       -> (() => MMSortTwoTest.run(args2), "mask merge sort"),
    "ConcatZeroStrip" -> (() => ConcatZeroStripTest.run(args2), "concat zero strip"),
    "NwayMux"         -> (() => NwayMuxTest.run(args2), "n-way MUX"),
    "Gray"            -> (() => GrayTest.run(args2), "gray coding"),
    "Fibonacci"       -> (() => FibonacciTest.run(args2), "Fibonacci number")
  )

  TestUtil.launch(args, targetmap)
}
