module readmemb_test;

   reg [15:0] data;
   reg [15:0] mem [0:15];
   reg [7:0] cnt;
   reg 	     clock;
   
	       
always  @(posedge clock or negedge clock)
  begin
     data <= mem[cnt];
     cnt <= cnt + 1;
  end

initial begin
   $readmemb("readmemb.dat", mem);
   $monitor ("cnt=%d data=%h", cnt, data);
   clock = 0;
   cnt=0;
   data = 0;
   #15 $finish;
end

always begin
   #1 clock = !clock;
end
   
endmodule
