package dummyone

object one {
  def apply(a: String) {
    println("one:" + a)
  }


  def func(a: String) {
    println("one.func:" + a)
  }

  /*
   // this object cannot be run as a standalone app?

   def main(args: Array[String]) {
    println("dummyone main")
  }
   */
}
