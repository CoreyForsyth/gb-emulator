package io.github.coreyforsyth.gbemulator.memory;

public class WorkRam implements ReadWrite
{
    final byte[] ram;

    public WorkRam()
    {
        this.ram = new byte[0x2000];
    }

    public int getOffset(int sp) {
        if (sp < 0xC000 || sp > 0xDFFF) {
            throw new RuntimeException("WorkRam access issue");
        }
        return sp - 0xC000;
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
