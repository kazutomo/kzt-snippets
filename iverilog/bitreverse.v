`timescale 10ns/10ns

module reverse8bit(
		   input clk,
		   input [7:0]  data_in,
		   output [7:0] data_out);
   
   integer 			    i;
   reg [7:0] 			    r;
   
   always @ (posedge clk)
     begin
	for (i=0; i<8; i=i+1) begin: reverse
	   r[8-1-i] <= data_in[i];
	end
     end

   assign data_out = r;
   
endmodule // reverse8bit


module reverse8bit_tb;
   
   reg clk;
   reg [7:0] din;
   wire [7:0] dout;
   reg [9:0]  cnt;
   
   reverse8bit r8b(clk, din, dout);
   
   always #1 clk = !clk;

   always @ (posedge clk)
     begin
	cnt <= cnt + 1;
     end
   
   initial begin
      $dumpfile("reverse8bit_tb.vcd");
      $dumpvars(0, reverse8bit_tb); // all variables in this module
      $monitor ("%02d din=%b dout=%b", cnt, din, dout);

      clk = 0;
      cnt = 0;
      
      din = 8'b11110000;
      #2 ;
      din = 8'b01000111;
      #2 ;
      din = 8'b00000110;
      #2 $finish;
   end // initial begin
endmodule // dualportrom_tb
