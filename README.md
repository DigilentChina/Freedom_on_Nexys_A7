Freedom 移植用フォークプロジェクトです。
=====================================

FreedomをSiFiveがサポートしているFPGAボード以外のボードに移植するプロジェクトです。

移植したボードは以下の通りです。

* [Nexys 4 DDR Artix-7 FPGA](https://reference.digilentinc.com/reference/programmable-logic/nexys-4-ddr/start) (Freedom E300を移植)

ビルドは、以下のように実行してください。

```sh
$ make -f Makefile.e300nexys4ddrdevkit verilog
$ make -f Makefile.e300nexys4ddrdevkit mcs
```

Freedom
=======

このリポジトリは、SiFiveによって作成された、Freedom E300、U500プラットフォームのRTLを含みます。
Freedom E310 Arty FPGA開発キットは、Freedom E300プラットフォームを実装し、[Arty FPGA Evaluation
Kit](https://www.xilinx.com/products/boards-and-kits/arty.html) に対して設計されたものです。
FreedomU500 VC707 FPGA開発キットは、Freedom U500プラットフォームを実装し、[VC707 FPGA Evaluation
Kit](https://www.xilinx.com/products/boards-and-kits/ek-v7-vc707-g.html) に対して設計された
ものです。
両システムは、自律的に起動し、外部デバッガーで制御されます。

このリポジトリの使い方の説明書については、関心のある開発Kitに対応する節を読んで下さい。

ソフトウェア要件
--------------------

Freedom E300 ArtyとU500 VC707 FPGA開発キットのブートローダーをコンパイルするために、
RISC-Vソフトウェア・ツールチェインをローカルにインストールして、$(RISCV)環境変数を
RISC-Vツールチェインがインストールされた場所を指すようにセットしなければなりません。
ツールチェインは、スクラッチからビルドする事もできますし、以下からダウンロードする事もできます。
https://www.sifive.com/products/tools/


Freedom E300 Arty FPGA開発キット
------------------------------

Freedom E300 Arty FPGA開発キットは、Freedom E300チップを実装しています。

### ビルド方法

Freedom E300 Arty FPGA開発キットに対応するMakeファイルは、`Makefile.e300artydevkit` です。
このMakeファイルは、2つの主なビルド・ターゲットからなります:

- `verilog`: Chiselソースファイルをコンパイルし、Verilogファイルを生成します。
- `mcs`: Arty FPGAをプログラムする事ができる、Configuration Memoryファイル(.mcs)を生成します。

これらのターゲットを実行するには、下記のコマンドを実行します:

```sh
$ make -f Makefile.e300artydevkit verilog
$ make -f Makefile.e300artydevkit mcs
```

注意: この手順は、Vivado 2017.1が必要です。古いバージョンは失敗する事が判明しています。

これらは、`builds/e300artydevkit/obj` にファイルを出力します。

`mcs` ターゲットを実行するには、vivado 実行ファイルが、`PATH`に含まれている必要があります。

### Bootrom

デフォルトのbootromは、アドレス0x20400000に即座にジャンプするプログラムで構成されています。
このプログラムは、ArtyボードのSPIフラッシュ・メモリの0x00400000バイト目に保存されます。

### 生成したMCSイメージの使用方法

生成したイメージをFPGAに載せて、ソフトウェアと一緒にプログラムする手順については、[Freedom E SDK](https://github.com/sifive/freedom-e-sdk)を使用して、[Freedom E310 Arty FPGA Dev Kit Getting Started Guide](https://www.sifive.com/documentation/freedom-soc/freedom-e300-arty-fpga-dev-kit-getting-started-guide/)を参照して下さい。

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
