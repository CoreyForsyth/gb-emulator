package io.github.coreyforsyth.gbemulator.memory;

public class ExternalRam implements ReadWrite
{
    final byte[] ram;
    int bank;

    public ExternalRam(byte[] rom)
    {
        this.ram = rom;
        this.bank = 0;
    }

    public int getOffset(int sp) {
        if (sp < 0xA000 || sp > 0xBFFF) {
            throw new RuntimeException("ExternalRam access issue");
        }
        return sp - 0xA000 + 0x2000 * bank;
    }

    public int size() {
        return ram.length / 0x3FFF;
    }

    @Override
    public byte read(char address)
    {
        return ram[getOffset(address)];
    }

    @Override
    public void write(char address, byte value)
    {
        ram[getOffset(address)] = value;
    }

}
