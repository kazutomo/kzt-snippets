
fn main() {

	let f: bool = false;
	let a: f64 = 1.234; // immutable by default
	// a = 0; // cannot assign since a is immutable
	let mut b: f64 = 22.0;

	b = a; // should work

	println!("{}, {}", f, b)
}
