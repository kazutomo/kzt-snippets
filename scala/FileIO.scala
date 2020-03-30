import scala.util.control._
import java.io._
import scala.io.Source

object FileIO {
  def main(args: Array[String]) {

    println("* Wriing to a file using Scala PrintWriter")
    val fn = "tmpoutput.txt"
    val w = new PrintWriter(new File(fn))
    w.write("""Hey!
Second line
Third line
""")
    w.close()

    println("* Reading a file in Scala Source.fromFile")
    Source.fromFile(fn).foreach { print }

    println("* Reading a file using Java FileReader")
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
  }
}
