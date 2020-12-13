//
// test driver
//
// written by Kazutomo Yoshii <kazutomo.yoshii@gmail.com>
// 
package foobar

import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}

object TestMain extends App {
  // default params and component target list
  val targetlist = List(
    "rev", "foo", "count",
    "concat", "xnor", "clz", "srmem", "nerd", "count",
    "bmsort"  )

  val a = if (args.length > 0) args(0) else "rev"
  val tmp = a.split(":")
  val target = tmp(0)
  val mode = if (tmp.length > 1) tmp(1) else "test"

  if ( target=="list" ) {
    println("*target list")
    for (t <- targetlist)  println(t)
    System.exit(0)
  }

  val verilogonly = mode.toLowerCase().substring(0,1) match {case "v" => true ; case _ => false}

  println(f"MODE=$mode TARGET=$target")

  target match {
    case "list" =>
      println("*target list")
      for (t <- targetlist)  println(t)
    case "rev" => RevTest.run(args, verilogonly)
    case "foo" => FooTest.run(args, verilogonly)
    case "count" => CounterTest.run(args, verilogonly)
    case "nerd" => NerdCounterTest.run(args, verilogonly)
    case "xnor" => XnorPopTest.run(args, verilogonly)
    case "clz" => ClzTest.run(args, verilogonly)
    case "srmem" => SRMemTest.run(args, verilogonly)
    case "concat" => ConcatVecsTest.run(args, verilogonly)
    case "bmsort" => BitMaskSortedTest.run(args, verilogonly)
    case _ => RevTest.run(args, verilogonly)
  }
}
