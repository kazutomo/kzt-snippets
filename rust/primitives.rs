
fn main() {
    // types
    // bool
    // integer, i|u{8,16,32,64,128}  isize usize?
    // float, f32, f64
    let bl: bool = false;
    let a: f64 = if bl {2.234} else {1.234}; // immutable by default. 'if' is an expression 
    // a = 0; // cannot change since it's immutable
    let mut b: f64 = 22.0;
    let d8: [i8; 4] = [1,2,3,4]; // array
    let tpl: (f32, i8) = (0.9, 100);

    println!("tpl={:?}", tpl); // {:?} is debug print

    println!("bl={}", bl);
    
    println!("a={}", a);
    println!("b={}", b);
    b = a; // should work
    println!("b={}", b);

    let a = a + 1.0; // shadowing
    println!("a={}", a);

    println!("{:?} {}", d8, d8.len());

    let ret = b.round() as i32;

    std::process::exit(ret - 1);
}
