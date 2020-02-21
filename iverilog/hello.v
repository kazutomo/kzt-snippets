module main;
   wire [3:0] y;
   assign y = 4'b1100;

   initial
     begin
	$display("Hello, World");
	$display("y=%b", y);
	$finish ;
     end
endmodule // main
