`timescale 1 ps / 1 ps

module reverse8bit(
		   input 	    clk,
		   input [7:0] 	    data_in,
		   output reg [7:0] data_out);
   
   integer 			    i;

   always @ (posedge clk)
     begin
	for (i=0; i<8; i=i+1) begin: reverse
	   data_out[8-1-i] <= data_in[i];
	end
     end
endmodule // reverse8bit

module my_rev8 (
		input 	      clock,
		input 	      resetn,
		input 	      ivalid,
		input 	      iready,
		output 	      ovalid,
		output 	      oready,
		input [7:0]   datain,
		output [7:0] dataout);

   assign ovalid = 1'b1;
   assign oready = 1'b1;
   // ivalid, iready, resetn are ignored   

   reverse8bit rev8(
		    clock,
		    datain,
		    dataout);
endmodule
