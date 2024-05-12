package io.github.coreyforsyth.gbemulator.memory;

public class RomOnlyCartridge implements Cartridge
{
    final byte[] rom;
    final byte[] ram;

    public RomOnlyCartridge(byte[] rom)
    {
        this.rom = rom;
        this.ram = new byte[0x8000];
    }

    public int getRamOffset(int sp) {
        return sp - 0xA000;
    }

    @Override
    public byte read(char address)
    {
        if (address < 0x8000) {
            return rom[address];
        } else if (address > 0x9FFF && address < 0xC000){
            return ram[getRamOffset(address)];
        }
        return 0;
    }

    @Override
    public void write(char address, byte value)
    {
        if (address > 0x9FFF && address < 0xC000) {
            ram[address] = value;
        }
    }
}
