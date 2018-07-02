// See LICENSE for license details.
// See LICENSE.HORIE_Tetsuya for license details.
package sifive.freedom.everywhere.e300nexys4ddrdevkit

import Chisel._

import freechips.rocketchip.config._
import freechips.rocketchip.subsystem._
import freechips.rocketchip.devices.debug._
import freechips.rocketchip.devices.tilelink._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.system._

import sifive.blocks.devices.mockaon._
import sifive.blocks.devices.gpio._
import sifive.blocks.devices.pwm._
import sifive.blocks.devices.spi._
import sifive.blocks.devices.uart._
import sifive.blocks.devices.i2c._
import sifive.blocks.devices.seg7._
import sifive.blocks.devices.vga._

//-------------------------------------------------------------------------
// E300Nexys4DDRDevKitSystem
//-------------------------------------------------------------------------

class E300Nexys4DDRDevKitSystem(implicit p: Parameters) extends RocketSubsystem
    with HasPeripheryMaskROMSlave
    with HasPeripheryDebug
    with HasPeripheryMockAON
    with HasPeripheryUART
    with HasPeripherySPIFlash
    with HasPeripherySPI
    with HasPeripheryGPIO
    with HasPeripheryPWM
    with HasPeripheryI2C
    with HasPeripherySeg7LED
    with HasPeripheryVGA {
  override lazy val module = new E300Nexys4DDRDevKitSystemModule(this)
}

class E300Nexys4DDRDevKitSystemModule[+L <: E300Nexys4DDRDevKitSystem](_outer: L)
  extends RocketSubsystemModuleImp(_outer)
    with HasPeripheryDebugModuleImp
    with HasPeripheryUARTModuleImp
    with HasPeripherySPIModuleImp
    with HasPeripheryGPIOModuleImp
    with HasPeripherySPIFlashModuleImp
    with HasPeripheryMockAONModuleImp
    with HasPeripheryPWMModuleImp
    with HasPeripheryI2CModuleImp
    with HasPeripherySeg7LEDModuleImp
    with HasPeripheryVGAModuleImp {
  // Reset vector is set to the location of the mask rom
  val maskROMParams = p(PeripheryMaskROMKey)
  global_reset_vector := maskROMParams(0).address.U
}
