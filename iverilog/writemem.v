

module bench;
   
   reg [63:0] data [255:0];
   // 64-bit data,   256 entries

   integer    i;
   
   initial begin
      $display("hi");
      
      for (i=0; i<256; i=i+1) begin
	 data[i] = i;
	 $display("%d", i);
	 
      end
      $writememh("dportrom.dat", data);
   end
endmodule // bench

