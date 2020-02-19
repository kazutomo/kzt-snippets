//
// test driver
//
// written by Kazutomo Yoshii <kazutomo.yoshii@gmail.com>
// 
package foobar

import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}

class RevUnitTester(c: Rev) extends PeekPokeTester(c) {

  val seed = 123
  val r = new scala.util.Random(seed)

  for (i <- 0 to 10) {
    val input = r.nextInt(255)
    poke(c.io.in, input)
    val output = peek(c.io.out).toInt // peek() returns bigInt

    printf("%08d => %08d\n",
      input.toBinaryString.toInt,
      output.toBinaryString.toInt)
  }
}

object TestMain extends App {
  // component target list.
  val targetlist = List(
    "rev"
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
  }
}
