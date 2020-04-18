object SString extends App {

  val n = 4

  // input a0, a1, ... a(n-1) from west
  // input b0, b1, ... b(n-1) from north

  for (col <- 0 until n) {
    for (row <- 0 until n) {
      val in_w  = if (col == 0) s"a${row}" else s"${row},${col-1}" // from west
      val out_e = if (col == (n-1)) "GND"  else s"${row},${col+1}" // to east
      val in_n  = if (row == 0) s"b${col}" else s"${row-1},${col}" // from north
      val out_s = if (row == (n-1)) "GND"  else s"${row+1},${col}" // to south

      println(s"PE($row,$col): in_w=$in_w out_e=$out_e in_n=$in_n out_s=$out_s")
    }
  }
}
