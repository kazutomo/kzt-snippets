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
    "concat", "xnor", "clz", "srmem", "nerd", "count")


  val a = if (args.length > 0) args(0) else "rev"
  val tmp = a.split(":")
  val target = tmp(0)
  val mode = if (tmp.length > 1) tmp(1) else "test"

  if ( target=="list" ) {
    println("*target list")
    for (t <- targetlist)  println(t)
    System.exit(0)
  }

  mode match {
    case "test" => {}
    case "verilog" => {}
    case _ => {
      println(f"Warning: $mode should be 'test' or 'verilog'")
      System.exit(1)
    }
  }
  println(f"MODE=$mode TARGET=$target")

  target match {
    case "list" =>
      println("*target list")
      for (t <- targetlist)  println(t)
    case "rev" =>
      val bitwidth = 8
      val dut = () => new Rev(bitwidth)
      val tester = c => new RevUnitTester(c)
      mode match {
        case "verilog" => chisel3.Driver.execute(args, dut)
        case _ => iotesters.Driver.execute(args, dut) {tester}
      }
    case "foo" =>
      val dut = () => new Foo()
      val tester = c => new FooUnitTester(c)
      mode match {
        case "verilog" => chisel3.Driver.execute(args, dut)
        case _ => iotesters.Driver.execute(args, dut) {tester}
      }
    case "count" =>
      val dut = () => new Counter()
      val tester = c => new CounterUnitTester(c)
      mode match {
        case "verilog" => chisel3.Driver.execute(args, dut)
        case _ => iotesters.Driver.execute(args, dut) {tester}
      }
    case "nerd" =>
      val dut = () => new NerdCounter()
      val tester = c => new NerdCounterUnitTester(c)
      mode match {
        case "verilog" => chisel3.Driver.execute(args, dut)
        case _ => iotesters.Driver.execute(args, dut) {tester}
      }
    case "xnor" =>
      val ninputs = 8
      val dut = () => new XnorPop(ninputs)
      val tester = c => new XnorPopUnitTester(c)
      mode match {
        case "verilog" => chisel3.Driver.execute(args, dut)
        case _ => iotesters.Driver.execute(args, dut) {tester}
      }
    case "clz" =>
      val nb = 16
      val dut = () => new ClzParam(nb)
      val tester = c => new ClzUnitTester(c)
      mode match {
        case "verilog" => chisel3.Driver.execute(args, dut)
        case _ => iotesters.Driver.execute(args, dut) {tester}
      }
    case "srmem" =>
      val dut = () => new SRMem()
      val tester = c => new SRMemUnitTester(c)
      mode match {
        case "verilog" => chisel3.Driver.execute(args, dut)
        case _ => iotesters.Driver.execute(args, dut) {tester}
      }
    case "concat" =>
      val dut = () => new ConcatVecs()
      val tester = c => new ConcatVecsUnitTester(c)
      mode match {
        case "verilog" => chisel3.Driver.execute(args, dut)
        case _ => iotesters.Driver.execute(args, dut) {tester}
      }
  }
}
