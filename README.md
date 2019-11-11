Fork from Freedom
=====================================

This is a project to port Freedom to a Nexys A7 FPGA board supported by SiFive.

FPGA Board
-----------------

The transplanted boards are as follows.

* [Nexys 4 DDR Artix-7 FPGA](https://reference.digilentinc.com/reference/programmable-logic/nexys-4-ddr/start) 
Ported Freedom E300 and U500

Build method
----------

### For Freedom E300

Please execute the build as follows.

```sh
$ make -f Makefile.e300nexys4ddrdevkit verilog
$ make -f Makefile.e300nexys4ddrdevkit mcs
```

### For Freedom U500

Please execute the build as follows.

```sh
$ make -f Makefile.u500nexys4ddrdevkit verilog
$ make -f Makefile.u500nexys4ddrdevkit mcs
```

At startup, the SD card MBR (Master Boot Record) is copied to the address 0x80000000 and executed.

Freedom
=======

This repository contains the RTL created by SiFive for its Freedom E300 and U500 platforms. The Freedom E310 Arty FPGA Dev Kit implements the Freedom E300 Platform and is designed to be mapped onto an Arty FPGA Evaluation Kit. The Freedom U500 VC707 FPGA Dev Kit implements the Freedom U500 Platform and is designed to be mapped onto a VC707 FPGA Evaluation Kit. Both systems boot autonomously and can be controlled via an external debugger.

Run the following commands to clone the repository and get started:

$ git clone https://github.com/sifive/freedom.git
$ cd freedom

#Run this command to update subrepositories used by freedom
$ git submodule update --init --recursive
Next, read the section corresponding to the kit you are interested in for instructions on how to use this repo.

Software Requirement
--------------------

To compile the boot loader of Freedom E300 Arty and U500 VC707 FPGA Development Kit,
Install the RISC-V software toolchain locally and set the $ (RISCV) environment variable
It must be set to point to the location where the RISC-V toolchain is installed.
Toolchains can be built from scratch or downloaded from:
https://www.sifive.com/products/tools/


Freedom E300 Arty FPGA Dev Kit
------------------------------

The Freedom E300 Arty FPGA Dev Kit implements a Freedom E300 chip.

### How to build

The Makefile corresponding to the Freedom E300 Arty FPGA Dev Kit is `Makefile.e300artydevkit` and it consists of two main targets:

- `verilog`: to compile the Chisel source files and generate the Verilog files.
- `mcs`: to create a Configuration Memory File (.mcs) that can be programmed onto an Arty FPGA board.

To execute these targets, you can run the following commands:

```sh
$ make -f Makefile.e300artydevkit verilog
$ make -f Makefile.e300artydevkit mcs
```

Note: This flow requires Vivado 2017.1. Old versions are known to fail.

These will place the files under `builds/e300artydevkit/obj`.

To run the `mcs` target, the vivado executable must be in the` PATH`.

### Bootrom

The default bootrom consists of a program that immediately jumps to address 0x20400000, which is 0x00400000 bytes into the SPI flash memory on the Arty board.

### Using the generated MCS Image

For instructions for getting the generated image onto an FPGA and programming it with software using the Freedom E SDK, please see the Freedom E310 Arty FPGA Dev Kit Getting Started Guide.

Freedom U500 VC707 FPGA Dev Kit
-------------------------------

The Freedom U500 VC707 FPGA Dev Kit implements the Freedom U500 platform.

### How to build

The Makefile corresponding to the Freedom U500 VC707 FPGA Dev Kit is
`Makefile.u500vc707devkit` and it consists of two main targets:

- `verilog`: to compile the Chisel source files and generate the Verilog files.
- `mcs`: to create a Configuration Memory File (.mcs) that can be programmed
onto an VC707 FPGA board.

To execute these targets, you can run the following commands:

```sh
$ make -f Makefile.u500vc707devkit verilog
$ make -f Makefile.u500vc707devkit mcs
```

Note: This flow requires Vivado 2016.4. Newer versions are known to fail.

These will place the files under `builds/u500vc707devkit/obj`.

Note that in order to run the `mcs` target, you need to have the `vivado`
executable on your `PATH`.

### Bootrom

The default bootrom consists of a bootloader that loads a program off the SD
card slot on the VC707 board.
