package testutil

import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}
import chisel3.experimental._


object TestUtil {

  var verilogonly = false

  def launch(args: Array[String], targetmap: Map[String, (() => Unit, String)]) {

    val mode   = args(0)
    val target = args(1)
    val args2 = args.drop(2)

    def printlist() {
      println("*target list")
      for (t <- targetmap.keys) {
        printf("%-15s : %s\n", t, targetmap(t)._2)
      }
    }

    def checkfirstcharnocap(s: String, c: String) : Boolean = if (s.toLowerCase().substring(0,1) == c ) true else false

    // check see if only verilog output
    verilogonly = checkfirstcharnocap(mode, "v")

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

    // call the run() method of the found test object (e.g.,
    // FooTest.scal for Foo)
    targetmap(found)._1()
  }

  // This function search an option that is a combination of an option
  // string and an integer value in args (e.g., -len 10). opt excludes
  // '-'.  dval is the default value. A calling example is
  // getoptint(args, "len", 16). If '-len' is found, the returned args
  // is the same as the input args and optval is dval. If found, the
  // returned args does not include '-len INT' and optval is INT.
  def getoptint(args: Array[String], opt: String, dval: Int) :
      (Array[String], Int) = {

    val hopt = "-" + opt
    val pos = args.indexOf(hopt)

    if(pos < 0 || pos+1 >= args.length) return (args, dval)

    return (args.patch(pos, Nil, 2),  args(pos+1).toInt)
  }

  def driverhelper[T <: MultiIOModule](args: Array[String], dutgen : () => T, testergen: T => PeekPokeTester[T]) {
    // Note: is chisel3.Driver.execute a right way to generate
    // verilog, or better to use (new ChiselStage).emitVerilog()?
    if (verilogonly) chisel3.Driver.execute(args, dutgen)
    else           iotesters.Driver.execute(args, dutgen) {testergen}
  }

  // convenient functions

  def intToBinStr(v : Int,  nbits: Int) = f"%%0${nbits}d".format(v.toBinaryString.toInt)

  def convLongToBinStr(v : Long,  nbits: Int) = {
    val s = v.toBinaryString
    val l = s.length
    val leadingzeros = "0" * (if (nbits > l) nbits-l else 0)

    leadingzeros + s
  }

  def convLongToHexStr(v : Long,  nbits: Int) = {
    val s = v.toHexString
    val l = s.length
    val maxnhexd = nbits/4 + (if ((nbits%4)>0) 1 else 0)
    val leadingzeros = "0" * (if (maxnhexd > l) maxnhexd-l else 0)

    leadingzeros + s
  }


}

