
module mul(
	   input clk,
	   input [7:0] a,
	   input [7:0] b,
	   output [15:0] c
	   );

   reg [15:0] 		 creg;

   always @(posedge clk) begin
      creg <= a * b;
   end

   assign c = creg;

endmodule // mul


module mul_tb;

   reg clk;
   reg [7:0] a;
   reg [7:0] b;
   wire [15:0] c;

   mul m1 (clk, a, b, c);

   always # 1 clk = !clk;

   initial begin
      $dumpfile("mul_tb.vcd");
      $dumpvars(0, mul_tb); // all variables in this module

      clk = 0;
      a = 2;
      b = 2;
      #2 ;
      b = 3;
      #1 ;
      
      # 5 $finish;
   end // initial begin

endmodule // mul_tb


      


   
	   
