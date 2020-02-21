`timescale 10ns/10ns

module detect (clk, rst, d, q, toggle, falling, rising);
   input clk, rst, d;
   output toggle, falling, rising;
   output reg 	  q;
   
   // delay one clock
   always @(posedge clk or negedge rst)
     if (~rst)
       q <= #1 1'b0;
     else
       q <= #1 d;

   assign toggle  = (q ^ d);
   assign falling = (q & ~d);
   assign rising  = (~q & d);
endmodule // detect

module detect_tb;
   /* Make a reset that pulses once. */
   reg clk, rst, d;
   wire q, toggle, falling, rising;
   
   detect d1 (clk, rst, d, q, toggle, falling, rising);

   /* Make a regular pulsing clock. */
   always # 1 clk = !clk;
	
   initial begin
      $dumpfile("detect_tb.vcd");
      $dumpvars(0,detect_tb); // all variables in this module
      clk = 0;
      rst = 1;
      d = 0;
      # 1 rst = 0;
      # 1 rst = 1;
      # 1 d = 1;
      # 4 d = 0;
      # 5 $finish;
   end // initial begin

endmodule // ff_tb
