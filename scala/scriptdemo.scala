// you can run
// scala scriptdemo.scala one two three ...

def imperativedemo() = {
  var i = 0
  while (i < args.length) {
    println(args(i))
    i += 1
  }
}

def functionaldemo() = args.foreach(a => println(a))


println("* imperative demo")
imperativedemo()

println("* functional demo")
functionaldemo()

for (i <- 5 to 4)  {
  println(i)
}
