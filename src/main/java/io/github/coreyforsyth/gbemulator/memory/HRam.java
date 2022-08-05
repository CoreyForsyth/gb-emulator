package io.github.coreyforsyth.gbemulator.memory;

public class HRam implements ReadWrite
{
    final byte[] ram;

    public HRam()
    {
        this.ram = new byte[0x007F];
    }

    public int getOffset(int sp) {
        if (sp < 0xFF80 || sp > 0xFFFE) {
            throw new RuntimeException("WorkRam access issue");
        }
        return sp - 0xFF80;
    }

    public int size() {
        return ram.length / 0x2000;
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
