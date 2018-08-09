// See LICENSE for license details.
package sifive.freedom.unleashed.u500nexys4ddrdevkit

import Chisel._
import chisel3.experimental.{withClockAndReset}

import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._

import sifive.blocks.devices.gpio._
import sifive.blocks.devices.pinctrl.{BasePin}

import sifive.fpgashells.shell.xilinx.u500nexys4ddrshell._
import sifive.fpgashells.ip.xilinx.{IOBUF}

//-------------------------------------------------------------------------
// PinGen
//-------------------------------------------------------------------------

object PinGen {
  def apply(): BasePin = {
    new BasePin()
  }
}

//-------------------------------------------------------------------------
// U500Nexys4DDRDevKitFPGAChip
//-------------------------------------------------------------------------

class U500Nexys4DDRDevKitFPGAChip(implicit override val p: Parameters)
    extends U500Nexys4DDRShell
    with HasDDR2 {

  //-----------------------------------------------------------------------
  // DUT
  //-----------------------------------------------------------------------

  // Connect the clock to the 50 Mhz output from the PLL
  dut_clock := clk50
  withClockAndReset(dut_clock, dut_reset) {
    val dut = Module(LazyModule(new U500Nexys4DDRDevKitSystem).module)

    //---------------------------------------------------------------------
    // Connect peripherals
    //---------------------------------------------------------------------

    connectSPI      (dut)
    connectUART     (dut)
    connectMIG      (dut)

    //---------------------------------------------------------------------
    // GPIO
    //---------------------------------------------------------------------

    val gpioParams = p(PeripheryGPIOKey)
    val gpio_pins = Wire(new GPIOPins(() => PinGen(), gpioParams(0)))

    GPIOPinsFromPort(gpio_pins, dut.gpio(0))

    gpio_pins.pins.foreach { _.i.ival := Bool(false) }
    gpio_pins.pins.zipWithIndex.foreach {
      case(pin, idx) => led(idx) := pin.o.oval
    }

    // tie to zero
    for( idx <- 7 to 4 ) { led(idx) := false.B }
  }

}
