`timescale 10ns/10ns

module dff_async_rst (clk, rst, d, q);
   input clk, rst, d;
   output reg q;

   always @(posedge clk or negedge rst)
     if (!rst) begin
	q <= 1'b0;
     end else begin
	q <= d;
     end
endmodule // dff_async_rst

module dff_sync_rst (clk, rst, d, q);
   input clk, rst, d;
   output reg q;

   always @(posedge clk)
     if (!rst) begin
	q <= 1'b0;
     end else begin
	q <= d;
     end
endmodule // ff_sync_rst

module ff_tb;
   reg clk, rst, d;
   wire q, q_async;

   dff_sync_rst dff1  (.clk(clk), .rst(rst), .d(d), .q(q));
   dff_async_rst dff2 (.clk(clk), .rst(rst), .d(d), .q(q_async));

   /* Make a regular pulsing clock. */
   always #2 clk = !clk;
	
   initial begin
      $monitor("%3t rst=%d d=%d q=%d qa=%d", $time, rst, d, q, q_async);
      $dumpfile("ff_tb.vcd");
      $dumpvars(0,ff_tb);
      clk = 0;
      rst = 1;
      d = 0;
      # 1 rst = 0;
      # 2 rst = 1;
      # 2 d = 1;
      # 2 d = 0;
      # 1 rst = 0;
      # 1 rst = 1;
      # 5 $finish;
   end // initial begin

endmodule // ff_tb
