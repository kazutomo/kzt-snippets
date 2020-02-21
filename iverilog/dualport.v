`timescale 10ns/10ns



module dualportrom(
		     input 		   clk,
		     input [7:0] addr_a,
		     input [7:0] addr_b,
		     output reg [63:0]   q_a,
		     output reg [63:0]   q_b);
   
   
   reg [63:0]   rom[255:0];

   initial
     begin
      $readmemh("dportrom.dat", rom);
     end

   always @ (posedge clk)
     begin
	q_a <= rom[addr_a];
	q_b <= rom[addr_b];
     end
endmodule // dual_port_rom


module dualportrom_tb;
   reg clk;
   reg [7:0] addr_a;
   reg [7:0] addr_b;
   wire [63:0] q_a;
   wire [63:0] q_b;

   dualportrom dpr1 (clk, addr_a, addr_b, q_a, q_b);

   always # 1 clk = !clk;

   initial begin
      $dumpfile("dualportrom_tb.vcd");
      $dumpvars(0, dualportrom_tb); // all variables in this module
      $monitor ("addr_a=%h addr_b=%h q_a=%h q_b=%h", addr_a, addr_b, q_a, q_b);

      clk = 0;
      addr_a = 0;
      addr_b = 0;
      #1 ;
      addr_a = 1;
      addr_b = 1;
      #1 ;
      addr_a = 10;
      addr_b = 10;
      #10 $finish;
   end // initial begin
endmodule // dualportrom_tb

