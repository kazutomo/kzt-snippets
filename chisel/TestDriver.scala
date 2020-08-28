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
    "rev", "dynamic", "xnor"
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

    case "xnor" =>
      val ninputs = 8
      mode match {
        case "verilog" => chisel3.Driver.execute(args, () => new XnorPop(ninputs))
        case _ => iotesters.Driver.execute(args, () => new XnorPop(ninputs)) {c => new XnorPopUnitTester(c) }
      }


    case "dynamic" =>
      mode match {
        case "verilog" => chisel3.Driver.execute(args, () => new DynamicBus())
        case _ => iotesters.Driver.execute(args, () => new DynamicBus()) {c => new DynamicBusUnitTester(c) }
      }
  }
}
