// See LICENSE for license details.
// See LICENSE.HORIE_Tetsuya for license details.
package sifive.freedom.everywhere.e300nexys4ddrdevkit

import Chisel._
import chisel3.core.{attach}
import chisel3.experimental.{withClockAndReset}

import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy.{LazyModule}

import sifive.blocks.devices.gpio._
import sifive.blocks.devices.spi._

import sifive.fpgashells.shell.xilinx.nexys4ddrshell.{Nexys4DDRShell}
import sifive.fpgashells.ip.xilinx.{IBUFG, IOBUF, PULLUP, PowerOnResetFPGAOnly, STARTUPE2}

//-------------------------------------------------------------------------
// E300Nexys4DDRDevKitFPGAChip
//-------------------------------------------------------------------------

class E300Nexys4DDRDevKitFPGAChip(implicit override val p: Parameters) extends Nexys4DDRShell {

  //-----------------------------------------------------------------------
  // Clock divider
  //-----------------------------------------------------------------------
  val slow_clock = Wire(Bool())

  // Divide clock by 256, used to generate 32.768 kHz clock for AON block
  withClockAndReset(clock_8MHz, ~mmcm_locked) {
    val clockToggleReg = RegInit(false.B)
    val (_, slowTick) = Counter(true.B, 256)
    when (slowTick) {clockToggleReg := ~clockToggleReg}
    slow_clock := clockToggleReg
  }

  //-----------------------------------------------------------------------
  // DUT
  //-----------------------------------------------------------------------

  withClockAndReset(clock_32MHz, ~ck_rst) {
    val dut = Module(new E300Nexys4DDRDevKitPlatform)

    //---------------------------------------------------------------------
    // SPI flash IOBUFs
    //---------------------------------------------------------------------

    STARTUPE2(dut.io.pins.qspi.sck.o.oval)
    IOBUF(qspi_cs,  dut.io.pins.qspi.cs(0))

    IOBUF(qspi_dq(0), dut.io.pins.qspi.dq(0))
    IOBUF(qspi_dq(1), dut.io.pins.qspi.dq(1))
    IOBUF(qspi_dq(2), dut.io.pins.qspi.dq(2))
    IOBUF(qspi_dq(3), dut.io.pins.qspi.dq(3))

    //---------------------------------------------------------------------
    // JTAG IOBUFs
    //---------------------------------------------------------------------

    dut.io.pins.jtag.TCK.i.ival := IBUFG(IOBUF(jd_2).asClock).asUInt

    IOBUF(jd_5, dut.io.pins.jtag.TMS)
    PULLUP(jd_5)

    IOBUF(jd_4, dut.io.pins.jtag.TDI)
    PULLUP(jd_4)

    IOBUF(jd_0, dut.io.pins.jtag.TDO)

    // mimic putting a pullup on this line (part of reset vote)
    SRST_n := IOBUF(jd_6)
    PULLUP(jd_6)

    // jtag reset
    val jtag_power_on_reset = PowerOnResetFPGAOnly(clock_32MHz)
    dut.io.jtag_reset := jtag_power_on_reset

    // debug reset
    dut_ndreset := dut.io.ndreset

    // UART
    IOBUF(uart_txd_in, dut.io.pins.gpio.pins(16))
    IOBUF(uart_rxd_out, dut.io.pins.gpio.pins(17))
    val iobuf_uart_cts = Module(new IOBUF())
    iobuf_uart_cts.io.I := false.B
    iobuf_uart_cts.io.T := false.B

    // Mirror outputs of GPIOs with PWM peripherals to RGB LEDs on Arty
    // assign RGB LED0 R,G,B inputs = PWM0(1,2,3) when iof_1 is active
    IOBUF(led0_r, dut.io.pins.gpio.pins(1))
    IOBUF(led0_g, dut.io.pins.gpio.pins(2))
    IOBUF(led0_b, dut.io.pins.gpio.pins(3))

    // Note that this is the one which is actually connected on the HiFive/Crazy88
    // Board. Same with RGB LED1 R,G,B inputs = PWM1(1,2,3) when iof_1 is active
    IOBUF(led1_r, dut.io.pins.gpio.pins(19))
    IOBUF(led1_g, dut.io.pins.gpio.pins(21))
    IOBUF(led1_b, dut.io.pins.gpio.pins(22))

    // Only 19 out of 20 shield pins connected to GPIO pins
    // Shield pin A5 (pin 14) left unconnected
    // The buttons are connected to some extra GPIO pins not connected on the
    // HiFive1 board
    IOBUF(btn_0, dut.io.pins.gpio.pins(15))
    IOBUF(btn_1, dut.io.pins.gpio.pins(30))
    IOBUF(btn_2, dut.io.pins.gpio.pins(31))

    val iobuf_btn_3 = Module(new IOBUF())
    iobuf_btn_3.io.I := ~dut.io.pins.aon.pmu.dwakeup_n.o.oval
    iobuf_btn_3.io.T := ~dut.io.pins.aon.pmu.dwakeup_n.o.oe
    attach(btn_3, iobuf_btn_3.io.IO)
    dut.io.pins.aon.pmu.dwakeup_n.i.ival := ~iobuf_btn_3.io.O & dut.io.pins.aon.pmu.dwakeup_n.o.ie

    // UART1 RX/TX pins are assigned to PMOD_D connector pins 0/1
    IOBUF(ja_0, dut.io.pins.gpio.pins(25)) // UART1 TX
    IOBUF(ja_1, dut.io.pins.gpio.pins(24)) // UART1 RX

    // Use the LEDs for some more useful debugging things
    IOBUF(led_0, ck_rst)
    IOBUF(led_1, SRST_n)
    IOBUF(led_2, dut.io.pins.aon.pmu.dwakeup_n.i.ival)
    IOBUF(led_3, dut.io.pins.gpio.pins(14))

    IOBUF(led_4, dut.io.pins.gpio.pins(4))
    IOBUF(led_5, dut.io.pins.gpio.pins(5))
    IOBUF(led_6, dut.io.pins.gpio.pins(6))
    IOBUF(led_7, dut.io.pins.gpio.pins(7))
    IOBUF(led_8, dut.io.pins.gpio.pins(8))
    IOBUF(led_9, dut.io.pins.gpio.pins(9))
    IOBUF(led_10, dut.io.pins.gpio.pins(10))
    IOBUF(led_11, dut.io.pins.gpio.pins(11))
    IOBUF(led_12, dut.io.pins.gpio.pins(26))
    IOBUF(led_13, dut.io.pins.gpio.pins(27))
    IOBUF(led_14, dut.io.pins.gpio.pins(28))
    IOBUF(led_15, dut.io.pins.gpio.pins(29))

    //---------------------------------------------------------------------
    // Unconnected inputs
    //---------------------------------------------------------------------

    dut.io.pins.aon.erst_n.i.ival       := ~reset_periph
    dut.io.pins.aon.lfextclk.i.ival     := slow_clock
    dut.io.pins.aon.pmu.vddpaden.i.ival := 1.U
  }
}
