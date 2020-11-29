//
// test driver
//
// written by Kazutomo Yoshii <kazutomo.yoshii@gmail.com>
// 
package foobar

import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}

object TestMain extends App {
  // component target list.
  val targetlist = List(
    "rev", "concat", "xnor", "clz", "srmem", "nerd", "count"
  )

  val a = if (args.length > 0) args(0) else "rev"
  val tmp = a.split(":")

  val target = tmp(0)
  val mode = if (tmp.length > 1) tmp(1) else "test"

  mode match {
    case "test" => {}
    case "verilog" => {}
    case _ => println(f"Warning: $mode is not a valid mode")
  }

  println(f"MODE=$mode TARGET=$target")

  // TODO: make this block concise since there are lots of repeating here. 
  target match {
    case "list" =>
      println("*target list")
      for (t <- targetlist)  println(t)
    case "rev" =>
      val bitwidth = 8
      mode match {
        case "verilog" => chisel3.Driver.execute(args, () => new Rev(bitwidth))
        case _ => iotesters.Driver.execute(args, () => new Rev(bitwidth)) {c => new RevUnitTester(c) }
      }
    case "foo" =>
      mode match {
        case "verilog" => chisel3.Driver.execute(args, () => new Foo())
        case _ => iotesters.Driver.execute(args, () => new Foo()) {c => new FooUnitTester(c) }
      }
    case "count" =>
      mode match {
        case "verilog" => chisel3.Driver.execute(args, () => new Counter())
        case _ => iotesters.Driver.execute(args, () => new Counter()) {c => new CounterUnitTester(c) }
      }
    case "nerd" =>
      mode match {
        case "verilog" => chisel3.Driver.execute(args, () => new NerdCounter())
        case _ => iotesters.Driver.execute(args, () => new NerdCounter()) {c => new NerdCounterUnitTester(c) }
      }

    case "xnor" =>
      val ninputs = 8
      mode match {
        case "verilog" => chisel3.Driver.execute(args, () => new XnorPop(ninputs))
        case _ => iotesters.Driver.execute(args, () => new XnorPop(ninputs)) {c => new XnorPopUnitTester(c) }
      }
    case "clz" =>
      val nb = 16
      mode match {
        case "verilog" => chisel3.Driver.execute(args, () => new ClzParam(nb))
        case _ => iotesters.Driver.execute(args, () => new ClzParam(nb)) {c => new ClzUnitTester(c) }
      }
    case "srmem" =>
      mode match {
        case "verilog" => chisel3.Driver.execute(args, () => new SRMem())
        case _ => iotesters.Driver.execute(args, () => new SRMem()) {c => new SRMemUnitTester(c) }
      }
    case "concat" =>
      mode match {
        case "verilog" => chisel3.Driver.execute(args, () => new ConcatVecs())
        case _ => iotesters.Driver.execute(args, () => new ConcatVecs()) {c => new ConcatVecsUnitTester(c) }
      }
  }
}
