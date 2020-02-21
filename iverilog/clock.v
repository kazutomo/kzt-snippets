// really simple example

module clock;
   
reg clock, a;

initial begin
   $display("time clk a");
   $monitor("%4d %3d %b", $time, clock, a);
   clock = 0;
   a = 0;
   #5 a = 1;
   #5 a = 0;
   #5 a = 1;
   #5 $finish;
end

always begin
   #5 clock = !clock;
end

endmodule
   
   
	  
   
