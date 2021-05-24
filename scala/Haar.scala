import java.io._

object Haar {
  // return (oddlist, evenlist)
  def forwardHaar(in: List[Int]) : (List[Int], List[Int]) = {
    // odd, high-pass
    val d = (in.sliding(2,2).map {a => a(1) - a(0)}).toList
    // even, low-pass
    val ev = (in.sliding(1,2)).flatten.toList
    val s = (ev zip d) map {a => a._1 + a._2/2}
    (d, s)
  }

  def backwardHaar(d: List[Int], s: List[Int]) : List[Int] = {
    // reverse
    val tmp = (s zip d) map {a => val e = a._1 - a._2/2; val o = e + a._2; List(e, o)}
    tmp.flatten
  }

  def dump(fn: String, data: List[Int]) {
    val w = new PrintWriter(new File(fn))
    data foreach {d => w.write(f"$d\n")}
    w.close()
  }

  def main(args: Array[String]) {
    val tmp = List(8,27,147,149,162,196,218,257,6,40,183,192,160,179,224,230,8,47,177,180,197,180,243,253,9,46,188,159,230,216,242,271,7,36,181,194,209,270,312,277,21,27,174,209,240,246,287,258,16,17,190,221,245,267,294,239,8,18,178,251,237,280,251,263)
    val inpdata = List.tabulate(8)  {idx => tmp.sliding(8,8).map {_(idx)}.toList}.flatten

    val (d, s) = forwardHaar(inpdata)
    println(inpdata)
    dump("in.txt", inpdata)
    dump("d.txt", d)
    dump("s.txt", s)

    val (sd, ss) = forwardHaar(s)
    dump("sd.txt", sd)
    dump("ss.txt", ss)
    val (ssd, sss) = forwardHaar(ss)
    dump("ssd.txt", ssd)
    dump("sss.txt", sss)

    val (sssd, ssss) = forwardHaar(sss)
    dump("sssd.txt", sssd)
    dump("ssss.txt", ssss)


    val outdata = backwardHaar(d,s)

    if (inpdata == outdata) println("Validated")
    else {
      println("Failed to validata")
      println(inpdata)
      println(outdata)
    }
  }
}
