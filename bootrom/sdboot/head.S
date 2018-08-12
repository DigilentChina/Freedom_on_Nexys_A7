// See LICENSE for license details.
#include <platform.h>
#include <smp.h>
#include "common.h"

#define GPIO_BASE 0x64002000
#define UART_BASE 0x64000000
	
  .section .text.init
  .option norvc
  .globl _prog_start
_prog_start:
  smp_pause(s1, s2)

	li t0, 1
	li t1, GPIO_BASE + GPIO_OUTPUT_EN
	sw t0, 0(t1)
	li t1, GPIO_BASE + GPIO_OUTPUT_VAL
	sw t0, 0(t1)
	
	li t2, 433		// 5,000,000 / 115200 - 1
	li t3, UART_BASE + UART_REG_DIV
	li t2, UART_TXEN
	li t3, UART_BASE + UART_REG_TXCTRL
	sh t2, 0(t3)
	li t2, 0x41
	li t3, UART_BASE
	sb t2, 0(t3)

  li sp, (PAYLOAD_DEST + 0x7fff000)
  call main
  smp_resume(s1, s2)
  csrr a0, mhartid
  la a1, dtb
  li s1, PAYLOAD_DEST
  jr s1

  .section .rodata
dtb:
  .incbin DEVICE_TREE
