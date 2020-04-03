
object LateEval extends App {

  def inc_when_enabled(f: Boolean) = if (enable && f) cnt += 1

  def inc_when_enabled_late(f: => Boolean) = if (enable && f) cnt += 1 // the expression f is evaluated in the function

  var cnt = 0
  var enable = true
  var z = 2
  inc_when_enabled(2/z == 1)
  println(cnt)
  inc_when_enabled_late(2/z == 1)
  println(cnt)

  enable = false
  z = 0

  //inc_when_enabled(2/z == 1) // this causes exception since it evaluates before calling the function
  //println(cnt)

  inc_when_enabled_late(2/z == 1) // this is ok since enable is not false and f is not evaluated in the function
  println(cnt)
}
