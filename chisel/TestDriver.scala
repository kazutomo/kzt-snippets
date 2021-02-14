//
// test driver
//
// written by Kazutomo Yoshii <kazutomo.yoshii@gmail.com>
// 
package foobar

import testutil._

object TestMain extends App {

  if (args.length < 2) {
    println("Usage: foobar.TestMain command target [options]")
    println("")
    System.exit(1)
  }

  val args2 = args.drop(2) // drop the command

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
    "Gray"            -> (() => GrayTest.run(args2), "gray coding"),
    "Fibonacci"       -> (() => FibonacciTest.run(args2), "Fibonacci number"),
    "FindBitPos"      -> (() => FindBitPosTest.run(args2), "Find the position of the first high bit from LSB"),
    // testing subcomponents of a compressor block. move out from here later
    "NwayMux"         -> (() => NwayMuxTest.run(args2), "n-way MUX"),
    "BitShuffle"      -> (() => BitShuffleTest.run(args), "Bit shuffling"),
    "MMSortTwo"       -> (() => MMSortTwoTest.run(args2), "mask merge sort"),
    "ConcatZeroStrip" -> (() => ConcatZeroStripTest.run(args2), "concat zero strip"),
    "ShuffleMerge"    -> (() => ShuffleMergeTest.run(args2), "shuffle merge"),
    "Comp128"         -> (() => Comp128Test.run(args2), "comp128")
  )

  TestUtil.launch(args, targetmap)
}
