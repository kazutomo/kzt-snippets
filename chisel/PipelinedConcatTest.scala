package foobar

import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.{Driver, PeekPokeTester}
import testutil._
import scala.collection._

class PipelinedConcatUnitTester(c: PipelinedConcat) extends PeekPokeTester(c) {

  val nelems_a = c.nelems
  val nelems_b = nelems_a
  val nelems_out = nelems_a + nelems_b
  val bw = c.bw
  val maxval = (BigInt(1) << bw) - BigInt(1)
  val nstages = log2Ceil(nelems_a)+1

  println(f"nelems=${nelems_a} nelems_out=${nelems_out} bw=$bw nstages=${nstages}")


  type Data = List[BigInt]
  type Mask = BigInt
  class DataMask(val d: Data, val m: Mask)

  // the mask can be more than 64-bit, so BigInt is needed here
  def mkmask(p: Data) : Mask = p.zipWithIndex.map {case (v, i) => if (v>0) BigInt(1)<<i.toInt else BigInt(0)} reduce (_|_)

  // generate a test data in List[BigInt]. l is the length of the
  // List. The value 'v' is fill for the first n elements and 0 is
  // filled for the rest.
  def mktestdat(l: Int, v: BigInt, n: Int) : Data = List.tabulate(l) {i => if (i<n) v+i else BigInt(0)}

  def gentestDataMask(l: Int, v: BigInt, n: Int) : DataMask = {
    val d = mktestdat(l, v, n)
    val m = mkmask(d)
    new DataMask(d, m)
  }

  def concat(a: DataMask, b: DataMask) : DataMask = {
    val ad_nz = a.d filter (_ != 0)
    val ad_z  = a.d filter (_ == 0)
    val d = ad_nz ::: b.d ::: ad_z
    val l = a.d.length
    val m = (b.m << l) | a.m

    new DataMask(d, m)
  }

  def runtest(a: DataMask, b: DataMask) {
    // fill the input data
    for (i <- 0 until nelems_a) poke(c.io.inA(i), a.d(i))
    for (i <- 0 until nelems_b) poke(c.io.inB(i), b.d(i))
    poke(c.io.inAmask, a.m)
    poke(c.io.inBmask, b.m)

    step(1)
    // compare with reference
    val ref = concat(a, b)
    expect(c.io.outmask, ref.m)
    for (i <- 0 until nelems_out ) expect(c.io.out(i), ref.d(i))

    // debugout
    val outmask = peek(c.io.outmask).toLong
    print("dut.mask=" + TestUtil.convIntegerToBinStr(outmask, nelems_out))
    println(" ref.mask=" + TestUtil.convIntegerToBinStr(ref.m, nelems_out))

    for (i <- 0 until nelems_out ) {
      print("dut=" + TestUtil.convIntegerToHexStr(peek(c.io.out(i)).toLong, nelems_out))
      println(" ref=" + TestUtil.convIntegerToHexStr(ref.d(i), nelems_out))
    }
    println()
  }
  //
  class testParam(val v_a:Int, val n_a:Int, val v_b:Int, val n_b:Int)

  val tdat = new mutable.Queue[testParam]
  tdat.enqueue(new testParam(0,0, 0,0))
  tdat.enqueue(new testParam(16,nelems_a, 0,0))
  tdat.enqueue(new testParam(0,0, 32,nelems_b))
  tdat.enqueue(new testParam(16,nelems_a, 32,nelems_b))
  for (i <- 1 until nelems_a) {
    tdat.enqueue(new testParam(16,i,  32,nelems_b))
  }

  val refdat = new mutable.Queue[DataMask]

  for (s <- 0 until tdat.length + nstages) {
    println(f"== step=$s =====================")

    if (tdat.length > 0) {
      val td = tdat.dequeue()
      val a = gentestDataMask(nelems_a, td.v_a, td.n_a)
      val b = gentestDataMask(nelems_b, td.v_b, td.n_b)

      for (i <- 0 until nelems_a) poke(c.io.inA(i), a.d(i))
      for (i <- 0 until nelems_b) poke(c.io.inB(i), b.d(i))
      poke(c.io.inAmask, a.m)
      poke(c.io.inBmask, b.m)

      refdat.enqueue(concat(a, b))
    }
    step(1)

    if (s >= nstages-1) {
      if (refdat.length > 0) {
        val ref = refdat.dequeue()
        //expect(c.io.outmask, ref.m)
        //for (i <- 0 until nelems_out ) expect(c.io.out(i), ref.d(i))

        // debugout
        val outmask = peek(c.io.outmask).toLong
        print("dut.mask=" + TestUtil.convIntegerToBinStr(outmask, nelems_out))
        println(" ref.mask=" + TestUtil.convIntegerToBinStr(ref.m, nelems_out))
        for (i <- 0 until nelems_out ) {
          print("dut=" + TestUtil.convIntegerToHexStr(peek(c.io.out(i)).toLong, nelems_out))
          println(" ref=" + TestUtil.convIntegerToHexStr(ref.d(i), nelems_out))
        }
        println()
      }
    }
  }


/*
  runtest(gentestDataMask(nelems_a, 0, 0),   gentestDataMask(nelems_b, 0, 0))
  runtest(gentestDataMask(nelems_a, 10, nelems_a), gentestDataMask(nelems_b, 0, 0))
  runtest(gentestDataMask(nelems_a, 0,  0),  gentestDataMask(nelems_b, 20, nelems_b))
  runtest(gentestDataMask(nelems_a, 10, nelems_a), gentestDataMask(nelems_b, 20, nelems_b))
  for (i <- 0 until 5) {
    runtest(gentestDataMask(nelems_a, 0, 0),   gentestDataMask(nelems_b, 0, 0))
  }
 */
}

object PipelinedConcatUnitTest {

  def run(args: Array[String]) {
    val (args2, n) = TestUtil.getoptint(args,  "n", 40)
    val (args3, bw) = TestUtil.getoptint(args2, "bw", 10)

    val dut = () => new PipelinedConcat(n, bw)
    val tester = c => new PipelinedConcatUnitTester(c)
    TestUtil.driverhelper(args3, dut, tester)
  }

  /*
  def run(args: Array[String]) {
    val (argsrest, opt) = TestUtil.getopts(args,
      Map("n" -> "2", "bw" -> "64"))

    val dut = () => new PipelinedConcat(opt("n").toInt, opt("bw").toInt)
    val tester = c => new PipelinedConcatUnitTester(c)
    TestUtil.driverhelper(argsrest, dut, tester)
  }
   */
}
