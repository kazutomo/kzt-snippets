
use std::env;

fn main()
{
    println!("Hey!");

    for a in env::args() {
	println!("{}", a);
    }
    println!("-----");
    for a in env::args_os() {
	println!("{:?}", a); // print original type
    }

    let a = 1.1;
    println!("a = {la}", la=a);
}
