module cnt32(input clock,
	       input 		 resetn,
	       output reg [31:0] counter);

   always @ (posedge clock)
     begin
	if (!resetn)
	  counter <= 1;
	else
	  counter <= counter + 1;
     end
endmodule // cnt32

// this is a test to see how clock is connected
module my_cnt32(
		                  input 	clock,
		                  input 	resetn,
		                  input 	ivalid,
		                  input 	iready,
		                  output 	ovalid,
		                  output 	oready,
		                  input [31:0] 	initval, // dummy. no input does not work
				  output [31:0] counter);

   assign ovalid = 1'b1;
   assign oready = 1'b1;

   cnt32 c1(clock, resetn, counter);

endmodule // my_cnt32
