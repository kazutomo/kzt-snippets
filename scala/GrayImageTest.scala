
import java.io._
import Array._

// import java.nio.ByteBuffer
// e.g.,
// ByteBuffer.wrap(Array[Byte](0, 0, 0, 11)).getInt
// ByteBuffer.allocate(4).putInt(123456789).order(java.nio.ByteOrder.nativeOrder).array()


object GrayImage {

  //def loadIntBuf() = {
  //}

  // to check the generated image
  // e.g., display -normalize -resize 4000% -size 8x4 -depth 8 tmp-intimg.gray
  // XXX: how to generalize the pixel type? type parameter would not work since FileOutputStream.write() does not accept it
  // XXX: What is the best way to define a 2D image structure? Array[Array[]] is a bit messy
  def storeIntImage(img : Array[Array[Int]], fn: String) = {
    try {
      val out = new FileOutputStream(fn)
      val h = img.size
      val w = img(0).size

      for (y <- 0 until h) {
        for (x <- 0 until w) {
          out.write(img(y)(x))
        }
      }
    } catch {
        case e: IOException => println("Failed to open")
    }
  }


}

object GrayImageTest extends App {
  val w = 8
  val h = 4 
  val image = Array.ofDim[Int](h, w)

  for (y <- 0 until image.size) {
    for (x <- 0 until image(0).size)  image(y)(x) = y * 10 + w
  }

  GrayImage.storeIntImage(image, "tmp-intimg.gray")
}
