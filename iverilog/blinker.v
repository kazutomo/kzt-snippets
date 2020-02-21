module blinker;


   reg clk, rst;

   wire blink;

   reg [2:0] cnt_d, cnt_q;

   assign blink = cnt_q[2];

   always @(cnt_q) begin
      cnt_d = cnt_q + 1'b1;
   end

   always @(posedge clk) begin
      if (rst) begin
	 cnt_q <= 3'b0;
      end else begin
	 cnt_q <= cnt_d;
      end
   end

   always begin
      #1 clk = !clk;
   end

initial begin
   $monitor ("clk=%b cnt_q=%b blink=%b", clk, cnt_q, blink);
   clk = 0;
   cnt_q = 0;
   cnt_d = 0;
   
   #100 $finish;
end
   
endmodule
