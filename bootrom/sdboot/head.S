// See LICENSE for license details.
#include <platform.h>
#include <smp.h>
#include "common.h"
#define UART_BASE 0x64000000
	
  .section .text.init
  .option norvc
  .globl _prog_start
_prog_start:
  li sp, (PAYLOAD_DEST + 0x7ff000)
  call main
//	li t2, 0x41             // t2 = 'A'
//	li t3, UART_BASE
//	sb t2, 0(t3)
	
  	li t0, 512
	la t1, hello_prg
	li t2, PAYLOAD_DEST
copy_img:	
	beqz t0, copy_img_end
	addi t0, t0, -1
	lb t3, 0(t1)
	sb t3, 0(t2)
	addi t1, t1, 1
	addi t2, t2, 1
	j copy_img
copy_img_end:	
//	li t2, 0x42             // t2 = 'B'
//	li t3, UART_BASE
//	sb t2, 0(t3)
	
  csrr a0, mhartid
  la a1, dtb
  li s1, PAYLOAD_DEST
  jr s1

  .section .rodata
dtb:	
  .incbin DEVICE_TREE
hello_prg:
  .incbin "hello.bin"
