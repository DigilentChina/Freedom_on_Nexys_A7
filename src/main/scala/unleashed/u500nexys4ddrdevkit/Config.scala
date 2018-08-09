// See LICENSE for license details.
package sifive.freedom.unleashed.u500nexys4ddrdevkit

import freechips.rocketchip.config._
import freechips.rocketchip.subsystem._
import freechips.rocketchip.devices.debug._
import freechips.rocketchip.devices.tilelink._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.system._
import freechips.rocketchip.tile._

import sifive.blocks.devices.gpio._
import sifive.blocks.devices.spi._
import sifive.blocks.devices.uart._

import sifive.fpgashells.devices.xilinx.xilinxnexys4ddrmig.{MemoryXilinxDDRKey,XilinxNexys4DDRMIGParams}

// Default FreedomUNexys4DDRConfig
class FreedomUNexys4DDRConfig extends Config(
  new WithNMemoryChannels(1) ++
  new WithNBigCores(4)       ++
  new BaseConfig
)

// Freedom U500 Nexys4 DDR Dev Kit Peripherals
class U500Nexys4DDRDevKitPeripherals extends Config((site, here, up) => {
  case PeripheryUARTKey => List(
    UARTParams(address = BigInt(0x64000000L)))
  case PeripherySPIKey => List(
    SPIParams(rAddress = BigInt(0x64001000L)))
  case PeripheryGPIOKey => List(
    GPIOParams(address = BigInt(0x64002000L), width = 4))
  case PeripheryMaskROMKey => List(
    MaskROMParams(address = 0x10000, name = "BootROM"))
})

// Freedom U500 Nexys4 DDR Dev Kit
class U500Nexys4DDRDevKitConfig extends Config(
  new WithNExtTopInterrupts(0)   ++
  new U500Nexys4DDRDevKitPeripherals ++
  new FreedomUNexys4DDRConfig().alter((site,here,up) => {
    case ErrorParams => ErrorParams(Seq(AddressSet(0x3000, 0xfff)), maxAtomic=site(XLen)/8, maxTransfer=128)
    case PeripheryBusKey => up(PeripheryBusKey, site).copy(frequency = 50000000) // 50 MHz hperiphery
    case MemoryXilinxDDRKey => XilinxVC707MIGParams(address = Seq(AddressSet(0x80000000L,0x40000000L-1))) //1GB
    case DTSTimebase => BigInt(1000000)
    case ExtMem => up(ExtMem).map(_.copy(size = 0x40000000L))
  })
)
