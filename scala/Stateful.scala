
object Stateful extends App {

  class Distance() {
    val mile2km = 1.60934

    var km : Double = _

    private var nupdates: Int = 0
    def nUpdates: Int = nupdates


    def miles = km / mile2km
    def miles_= (m: Double) {  // Note: 'miles_=' should be one word. 
      km = m * mile2km
      nupdates += 1
    }

    override def toString = miles + " miles / " + km + " km"
  }

  var d = new Distance()
  d.miles = 1
  println(d)

  d.km = 1
  println(d)

  println(d.nUpdates)
  // d.nUpdates = 1  // cannot update since there is no setter
}
