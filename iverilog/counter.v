module counter(
	       input clk, reset,
	       input copy,
	       output reg [15:0] val
	       );
   reg [15:0] 	cnt;

   always @ (posedge clk)
     begin
	if (reset)
	  cnt <= 0;
	else
	  cnt <= cnt + 1;
     end

   always @ (posedge clk)
     begin
	if (copy)
	  val <= cnt;
     end
endmodule // counter


module counter_tb;

   /* Make a reset that pulses once. */
   reg reset = 0;
   reg copy = 0;
   initial begin
      $dumpfile("counter.vcd");
      $dumpvars(0,counter_tb);
      #1 reset = 1;
      #1 reset = 0;
      #5 copy = 1;
      #2 copy = 0;
      #1 copy = 1;
      #2 copy = 0;
      #10 $finish;
   end // initial begin

   /* Make a regular pulsing clock. */
   reg clk = 0;

   always #1 clk = !clk;

   wire [15:0] stamp;
   counter c1 (clk, reset, copy, stamp);

//   initial
//     $monitor("At time %t, stamp=%d",
//	      $time, stamp);

endmodule // counter_tb

   