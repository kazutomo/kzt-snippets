import Array._

class ImageData(val width: Int, val height: Int) {
  var pixels = ofDim[Int](height, width)

  def set(x: Int, y: Int, v: Int) : Unit = pixels(y)(x) = v

  def get(x: Int, y: Int) : Int = pixels(y)(x)

  // statistical info
  var maxval = 0
  var zerocnt = 0

  def se() : Unit = maxval = maxval + 1

  def reset(v: Int = 0) : Unit = for(y <- 0 until height; x <- 0 until width) set(x, y, v)

}


object Hello extends App {
  printf("Hello %s\n", "World")

  val z = new ImageData(10,10)
  val x = 1
  val y = 2
  println(z.get(x,y))
  z.reset()
  println(z.get(x,y))
  z.set(x, y, 123)
  println(z.get(x,y))
  println(z.width)
  println(z.maxval)
  z.se()
  println(z.maxval)
}
