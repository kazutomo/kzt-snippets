import scala.util.control._
import java.io._
import scala.io.Source

object FileIO {
  def main(args: Array[String]) {

    for (a <- args.drop(1)) {
      println(a)
    }

    val fn = "tmpoutput.txt"
    val w = new PrintWriter(new File(fn))
    w.write("Hey!\n")
    w.close()

    // reading in java way
    val r = new FileReader(fn)
    var str : String = ""
    var c : Int = 0
    val inner = new Breaks;

    inner.breakable {
      while (true) {
        c = r.read()
        if ( c == -1 ) inner.break
        str += c.asInstanceOf[Char]
      }
    }
    r.close()
    print(str)

    // reading
    Source.fromFile(fn).foreach {
      print
    }
  }
}
